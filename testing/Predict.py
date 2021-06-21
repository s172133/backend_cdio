import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
from predictfunc import  Parameter,load_model,predictJava
import sys
import getopt
import time

def main(datavalg,img_path):

    """
    datavalg = sys.argv[1]

    img_path = sys.argv[2]
    """

  #  print(datavalg)
   # print(img_path)
    p=Parameter(32,180,180)
    train_ds = None
    val_ds = None    
    data_dir= None  
    #read_data(datavalg)
    #train_ds = training_data(p) 
    #val_ds = valid_data(p)
    #model = load_model(datavalg)
    datavalg = int (datavalg)
    if(datavalg == 1): 
        text = predictJava(p,model1,datavalg,img_path)
    elif(datavalg == 2):
        text = predictJava(p,model2,datavalg,img_path)
    
    
    
    return text
    
    #sourceFile = open('C:/Users/krist/Code/IdeaProjects/FindFirkanter/src/com/company/demo.txt','w')
    #print(text,file = sourceFile)
    #sourceFile.close()



model1 = load_model(1)
model2 = load_model(2)    
s = sys.stdin.readline().strip()
while s not in ['quit']:
    val = s.split(',') 
    text = main(val[0],val[1])
    sys.stdout.write(text+'\n')
    sys.stdout.flush()
    s = sys.stdin.readline().strip()
    
