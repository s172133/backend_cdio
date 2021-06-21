import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
import numpy 
import tensorflow as tf 
from tensorflow import keras
from tensorflow.keras import layers
from tensorflow.keras.models import Sequential

class Parameter:
    def __init__(self, batch_size,img_height,img_width):
        self.batch_size = batch_size
        self.img_height = img_height
        self.img_width = img_width

def load_model(datavalg):
    valg = int(datavalg)
    
    if(valg == 1):
        navn = "C:/Users/krist/Code/IdeaProjects/FindFirkanter/src/com/company/Kulor_model"
        model = keras.models.load_model(navn)
      #  print("loaded")
    elif(valg == 2):
        navn = "C:/Users/krist/Code/IdeaProjects/FindFirkanter/src/com/company/Tal_model"
        model = keras.models.load_model(navn)
       # print("loaded")
    else:
       # print("no model chosen\n")
        return None
   
    return model

def predictJava(Parameter, model,datavalg,img_path):
    

    #sunflower_path = "C:\\Users\\krist\\Code\\IdeaProjects\\FindFirkanter\\ciffer.jpg" 
    if datavalg == 1:
        class_names = ['hjerter', 'klor', 'ruder', 'spar']
    if datavalg == 2:
        class_names = [ '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'J', 'K', 'Q', 'X']
    

    img= keras.preprocessing.image.load_img(
        img_path, target_size=(Parameter.img_height,Parameter.img_width)
    )
    img_array = keras.preprocessing.image.img_to_array(img)
    img_array = tf.expand_dims(img_array,0)

    predictions = model.predict(img_array)
    score = tf.nn.softmax(predictions[0])
    
    return "{} {:.2f} procent".format(class_names[numpy.argmax(score)], 100 *numpy.max(score))