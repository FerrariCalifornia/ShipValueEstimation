#################### step1.训练集数据预处理 ####################

library(stringr)
library(lattice)
library(grid)
library(DMwR)
library(car)

setwd("/home/cc/Documents/ship")
#1.read and preprocess data---------------
#from last modeling result, we find type, port, level, district, length, width, height, grosston, enginepower, deadweight, builddate, dealdate are important
shipdata <- read.csv("data_train.csv",header = T,stringsAsFactors = F,na.strings = c("","/","\\","资料未显示","无"))
ennames <- c("shipindex","type","port","level","district","length","width","height"
             ,"grosston","deadweight","enginetype","enginepower"
             ,"builddate","factory","dealdate","dealamount")
names(shipdata) <- ennames

shipdata <- shipdata[complete.cases(shipdata[,!colnames(shipdata) %in% c("factory","enginetype")]),]   #删除 除主机型号，建造船厂外 变量为空 的样本
shipdata$train <- TRUE   #给shipdata增加一列

#后面会用到这个函数
getLastMeanValue <- function(df,col,curdate,days){
  mean(df[df$date>=(curdate-days) & df$date<=curdate,col])
}

#中国船舶交易价格指数
price.deal <- read.csv("j.csv",header = T,colClasses = c("Date","numeric"))
names(price.deal) <- c("date","j")
#航运景气指数
hangyunjingqi <- read.csv("i.csv",header = T,colClasses = c("character","numeric","numeric","numeric","numeric"))
names(hangyunjingqi) <- c("season","i1","i2","i3","i4")
#10mm造船板价格
price10mm <- read.csv("p10.csv",header = T,colClasses = c("Date","numeric","factor"))
names(price10mm) <- c("date","price10mm","city")
#20mm造船板价格
price20mm <- read.csv("p20.csv",header = T,colClasses = c("Date","numeric","factor"))
names(price20mm) <- c("date","price20mm","city")

#计算同一天，所有城市的均价
price10mm.day <- aggregate(price10mm$price10mm,by = list(price10mm$date),mean)
names(price10mm.day) <- c("date","price10mm")
#计算同一月，所有城市的均价
price10mm <- aggregate(price10mm.day$price10mm,by=list(substr(gsub("-","",price10mm.day$date),1,6)),mean)
names(price10mm) <- c("month","price10mm")

#计算同一天，所有城市的均价
price20mm.day <- aggregate(price20mm$price20mm,by = list(price20mm$date),mean)
names(price20mm.day) <- c("date","price20mm")
#计算同一月，所有城市的均价
price20mm <- aggregate(price20mm.day$price20mm,by=list(substr(gsub("-","",price20mm.day$date),1,6)),mean)
names(price20mm) <- c("month","price20mm")

price.iron <- merge(price10mm,price20mm,by="month")


#shipdata中enginetype是字符型变量，进行简化
shipdata$enginetype<-sapply(as.character(shipdata$enginetype),function(x){
  if(is.na(x))x  #若字符串为空，还为空
  else{
    nums <- unlist(str_extract_all(x,"[0-9]+"))   #nums为字符串中所有数字
    if(length(nums)>=1&&nchar(nums[1])>=3)nums[1]  #若字符串中有数字，且第一串连在一起的数字大于等于3位————第一串连在一起的数字
    else{
      substr(x,1,2) #字符串第一和第二位
    }
  }
})


#choose top 10 to retain and achieve 73.7% distribution cover.这里后期可以把代码改的更通用一些：找出频数最高的前10个再赋值
shipdata$enginetype <- factor(shipdata$enginetype, levels=c('6135','KT','6170','128','855','615','6160','WP','YC','618'))
sum(is.na(shipdata$enginetype))  #NA的个数：230，总共868

shipdata <- shipdata[!is.na(shipdata$dealamount) & shipdata$dealamount>0 ,]    #剔除交易额为空、交易额小于等于0的样本

#filling missing values using knn-----------------
#we do not fill in builddate and dealdate since these two must be provided by clients.
#第一步：把shipdata中所有字符串变量（factorNames）改成因子（类型）变量
factorNames <- c("type","port","level","district","factory","enginetype")
for(name in factorNames){
  # name <- "type"
  shipdata[,name] <- factor(as.character(shipdata[,name]))
}

#第二步：把shipdata中除colNotInKNN外所有变量（包括数值型和类型/因子型）用knn填补缺失值
colNotInKNN <- c("shipindex","builddate","dealdate","train")
#对shipdata中剔除colNotinKNN中的变量，对剩下的列中的缺失值使用KNN插值来处理
shipdata.cmp <- knnImputation(shipdata[,!names(shipdata) %in% colNotInKNN])  
shipdata.cmp <- cbind(shipdata.cmp,shipdata[,colNotInKNN])

#transform data format-------------
shipdata.cmp$builddate <- as.Date(shipdata.cmp$builddate,format="%m/%d/%Y")
shipdata.cmp$dealdate <- as.Date(shipdata.cmp$dealdate,format="%m/%d/%Y")

#generate other features------------
shipdata.cmp$diffweeks <- difftime(shipdata.cmp$dealdate,shipdata.cmp$builddate,units = "weeks")
shipdata.cmp$diffweeks <- as.numeric(shipdata.cmp$diffweeks)
shipdata.cmp <- subset(shipdata.cmp,subset=shipdata.cmp$diffweeks>0)
fivenum(shipdata.cmp$diffweeks)

shipdata.cmp$dealyear <- as.numeric(format(shipdata.cmp$dealdate,"%Y"))
shipdata.cmp$dealmonth <- as.numeric(format(shipdata.cmp$dealdate,"%m"))

shipdata.cmp$volumn <- with(shipdata.cmp, length*width*height)
shipdata.cmp$sparea <- with(shipdata.cmp, length*width+2*(width*height+length*height))   #无顶表面积

# shipdata.cmp$month <- format(shipdata.cmp$dealdate,"%Y%m")
shipdata.cmp$season <- paste(shipdata.cmp$dealyear,(shipdata.cmp$dealmonth-1)%/%3+1,"Q",sep = "")

# shipdata.cmp <- merge(shipdata.cmp,price.iron,by="month")
shipdata.cmp <- merge(shipdata.cmp,hangyunjingqi,by="season")
# shipdata.cmp <- merge(shipdata.cmp,j.month,by="month")

# getLastMeanValue(price.deal,"j",as.Date("2016-10-28"),20)
# for(i in 1:nrow(shipdata.cmp)){
#   shipdata.cmp[i,"j30days"] <-  getLastMeanValue(price.deal,"j",shipdata.cmp[i,"dealdate"],30)
# }
#交易日前60天内的平均船舶交易交割指数price.deal$j
for(i in 1:nrow(shipdata.cmp)){
  shipdata.cmp[i,"j60days"] <-  getLastMeanValue(price.deal,"j",shipdata.cmp[i,"dealdate"],100)
}
#交易日前90天内的平均船舶交易交割指数price.deal$j
for(i in 1:nrow(shipdata.cmp)){
  shipdata.cmp[i,"j90days"] <-  getLastMeanValue(price.deal,"j",shipdata.cmp[i,"dealdate"],110)
}
#交易日前30天内的10mm造船板的价格price10mm$i
for(i in 1:nrow(shipdata.cmp)){
  shipdata.cmp[i,"10mm30days"] <-  getLastMeanValue(price10mm.day,"price10mm",shipdata.cmp[i,"dealdate"],100)
}
for(i in 1:nrow(shipdata.cmp)){
  shipdata.cmp[i,"10mm60days"] <-  getLastMeanValue(price10mm.day,"price10mm",shipdata.cmp[i,"dealdate"],110)
}
#交易日前30天内的20mm造船板的价格price20mm$i
for(i in 1:nrow(shipdata.cmp)){
  shipdata.cmp[i,"20mm30days"] <-  getLastMeanValue(price20mm.day,"price20mm",shipdata.cmp[i,"dealdate"],100)
}
for(i in 1:nrow(shipdata.cmp)){
  shipdata.cmp[i,"20mm60days"] <-  getLastMeanValue(price20mm.day,"price20mm",shipdata.cmp[i,"dealdate"],110)
}
shipdata.cmp$season <- NULL      #删除掉shipdata.cmp中season那列
# shipdata.cmp$month <- NULL
# sink("shipvalue-message.txt")

#2.regression model and optimization--------------
#ignore the factory impact by removing the factoryinfluence feature.
# xs <- paste(setdiff(colnames(shipdata.cmp)
#                     ,c("dealdate","builddate","factory","train","shipindex","dealamount","factoryinfluence")),sep="",collapse = "+")
excludevars <- c("dealdate","builddate","factory","train","shipindex")   
#线性回归的自变量除去excludevars
xs <- paste(excludevars,sep="",collapse = "-")
# "dealdate-builddate-factory-train-shipindex"
(xs <- paste("log(dealamount)~.-",xs,sep=""))   
# "log(dealamount)~.-dealdate-builddate-factory-train-shipindex"
full.lm <- lm(xs,shipdata.cmp[shipdata.cmp$train,],na.action = "na.omit")

# library(MASS)
# full.lm <- lm.ridge(xs,shipdata.cmp[shipdata.cmp$train,],na.action = "na.omit")

# full.lm <- lm(log(dealamount)~.-dealdate-builddate-factory-train-shipindex,shipdata.cmp[shipdata.cmp$train,],na.action = "na.omit")
summary(full.lm)
# plot(full.lm)


#2.2remove outlier impact, change them from train data to test data.--------------
shipdata.cmp.shaved <- shipdata.cmp
# shipdata.cmp.shaved$factoryinfluence <- NULL
(outliers <- outlierTest(full.lm))    #library(car)
#回归诊断，剔除离群点样本(6个): Bonferroni离群点检验
shipdata.cmp.shaved <- shipdata.cmp.shaved[!row.names(shipdata.cmp.shaved) %in% names(outliers$rstudent),]

# 处理完毕的训练集：shipdata.cmp.shaved




################### step2.把训练集中的因子型变量变成哑变量 ####################

library(dummies)

data <- shipdata.cmp.shaved

#把因子型变量单独分出来成一个数据框，剩下的一个数据框
names_factor <- c("type","port","level","district","enginetype")
data_train_factor <- data[, names_factor]
data_train_num <- data[, !names(shipdata.cmp.shaved) %in% names_factor]

#用one-hot编码把因子型数据框0-1化
data_train_factor01 <- dummy.data.frame(data_train_factor)
data_train_factor01$shipindex <- shipdata.cmp.shaved$shipindex

#因子型数据框和余下的合并
data_train <- merge(data_train_num, data_train_factor01, by="shipindex")

#把不参与训练的自变量删掉
names_nottrain <- c("dealdate","builddate","factory","train","shipindex")
data_train_all <- data_train[, !names(data_train) %in% names_nottrain]
data_train_label <- data_train_all$dealamount
data_train <- subset(data_train_all, select= -dealamount)







################### step3.训练模型（GBDT算法）####################

data_train_all$dealamount <- log(data_train_all$dealamount)

library(gbm)
set.seed(1)
boosting <- gbm(dealamount~., data = data_train_all, distribution = "gaussian", n.trees = 4800, interaction.depth = 11, shrinkage = 0.00028)




################# step4.预测函数#################################
shipvalueEstimation <-
  function(predictcsv){
    
    ################## step1.测试集读入 #######################
    testdata <- read.csv(predictcsv,header = T,stringsAsFactors = F,na.strings = c("","/","\\","资料未显示","无"))
    ennames <- c("shipindex","type","port","level","district","length","width","height"
                 ,"grosston","deadweight","enginetype","enginepower"
                 ,"builddate","factory","dealdate")
    names(testdata) <- ennames
    
    ################## step2.测试集预处理 ######################
    testdata$train <- FALSE     #ncol=16
    
    #transform data format-------------
    testdata$builddate <- as.Date(testdata$builddate,format = "%m/%d/%Y")
    testdata$dealdate <- as.Date(testdata$dealdate,format = "%m/%d/%Y")
    #generate other features------------
    testdata$diffweeks <- difftime(testdata$dealdate,testdata$builddate,units = "weeks")
    testdata$diffweeks <- as.numeric(testdata$diffweeks)
    # testdata <- subset(testdata,subset=testdata$diffweeks>0)
    # fivenum(testdata$diffweeks)
    testdata$dealyear <- as.numeric(format(testdata$dealdate,"%Y"))
    testdata$dealmonth <- as.numeric(format(testdata$dealdate,"%m"))
    testdata$volumn <- with(testdata, length * width * height)
    testdata$sparea <- with(testdata, length * width + 2 * (width * height + length * height))
    testdata$month <- format(testdata$dealdate,"%Y%m")
    testdata$season <- paste(testdata$dealyear,(testdata$dealmonth - 1) %/% 3 + 1,"Q",sep = "")
    
    testdata <- merge(testdata,hangyunjingqi,by = "season")
    
    for (i in 1:nrow(testdata)) {
      testdata[i,"j60days"] <-
        getLastMeanValue(price.deal,"j",testdata[i,"dealdate"],100)
    }
    for (i in 1:nrow(testdata)) {
      testdata[i,"j90days"] <-
        getLastMeanValue(price.deal,"j",testdata[i,"dealdate"],110)
    }
    for (i in 1:nrow(testdata)) {
      testdata[i,"10mm30days"] <-
        getLastMeanValue(price10mm.day,"price10mm",testdata[i,"dealdate"],100)
    }
    for (i in 1:nrow(testdata)) {
      testdata[i,"10mm60days"] <-
        getLastMeanValue(price10mm.day,"price10mm",testdata[i,"dealdate"],110)
    }
    for (i in 1:nrow(testdata)) {
      testdata[i,"20mm30days"] <-
        getLastMeanValue(price20mm.day,"price20mm",testdata[i,"dealdate"],100)
    }
    for (i in 1:nrow(testdata)) {
      testdata[i,"20mm60days"] <-
        getLastMeanValue(price20mm.day,"price20mm",testdata[i,"dealdate"],110)
    }
    testdata$season <- NULL
    testdata$month <- NULL
    testdata$dealamount <- NA
    
    library(DMwR)
    #filling missing values using knn-----------------
    #we do not fill in builddate and dealdate since these two must be provided by clients.
    factorNames <- c("type","port","level","district","factory","enginetype")
    for (name in factorNames) {
      testdata[,name] <- factor(as.character(testdata[,name])
                                ,levels = unique(as.character(shipdata.cmp.shaved[shipdata.cmp.shaved$train,name])))
    }
    
    # 用KNN弥补缺失值
    colNotInKNN <- c("shipindex","builddate","dealdate","train")
    testdata.knn <-
      knnImputation(testdata[,!names(testdata) %in% colNotInKNN]
                    ,distData = shipdata.cmp.shaved[,!names(shipdata.cmp.shaved) %in% colNotInKNN])  # ʹ??KNN??ֵ
    testdata <- cbind(testdata.knn,testdata[,colNotInKNN])
    
    
    ################# step3.测试集中因子型变量变成哑变量 ################
    library(dummies)
    
    names_factor <- c("type","port","level","district","enginetype")
    data_test_factor <- testdata[, names_factor]
    data_test_num <- testdata[,!names(testdata) %in% names_factor]
    
    #用one-hot编码把因子型数据框0-1化
    data_test_factor01 <- dummy.data.frame(data_test_factor)
    data_test_factor01$shipindex <- testdata$shipindex
    
    #因子型数据框和余下的合并
    data_test <- merge(data_test_num, data_test_factor01, by="shipindex")
    
    #把不参与训练的自变量删掉
    names_nottrain <- c("dealdate","builddate","factory","train","shipindex")
    data_test_all <- data_test[, !names(data_test) %in% names_nottrain]
    
    
    ####################################################
    #测试集列数和训练集保持一致
    a <- names(data_train_all[,!colnames(data_train_all) %in% colnames(data_test_all)])
    data_test_all[,a] <- 0
    #ncol(data_train_all)
    #ncol(data_test_all)  #看是否测试集和训练集列数相同   
    
    paixu<- names(data_train_all)               #测试集和训练集每一列列名保持一致
    data_test_all <- data_test_all[,paixu]
    
    data_test_label <- data_test_all$dealamount
    data_test <- subset(data_test_all, select= -dealamount)
    
    
    
    ################# step4.预测结果 #########################
    library(gbm)
    
    predict_boosting <- predict(boosting, newdata = data_test, n.trees = 4800)
    
    
    ################# step5.返回结果 #########################
    
    write.csv(exp(predict_boosting), file = "jieguo.csv")
    
    
  }





save.image(paste("train_",format(Sys.Date(),"%Y%m%d"),".RData",sep=""))
