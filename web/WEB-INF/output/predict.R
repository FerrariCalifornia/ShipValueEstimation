setwd("/home/cc/Documents/ship")
modelfile <- max(list.files(pattern="*.RData"))
load(modelfile)
arg <- commandArgs(T) 
shipvalueEstimation(arg[1])

