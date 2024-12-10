# Import Library
from fastapi import FastAPI
import pandas as pd
import numpy as np
import tensorflow as tf
import nltk
from nltk.corpus import stopwords
from Sastrawi.Stemmer.StemmerFactory import StemmerFactory
import pickle

nltk.download('stopwords')

app = FastAPI()

#Load Data
stopWords = stopwords.words('indonesian')

factory = StemmerFactory()
stemmer = factory.create_stemmer()

with open('./data/dataset.pkl', 'rb') as file:
    patterns = pickle.load(file)

modelChatbot = tf.keras.models.load_model('./model/chatbotModel.h5')

modelClass = tf.keras.models.load_model('./model/classfication.h5')

with open('./data/responses.pkl','rb') as file:
    responses = pickle.load(file)

with open('./data/labelVocab.pkl', 'rb') as file:
    labelVocab = pickle.load(file)

#Preprossesing
def standardizeFunc(sentence):
    stopwords = stopWords
    
    sentence = tf.strings.lower(sentence)

    for word in stopwords:
        if word[0] == "'":
            sentence = tf.strings.regex_replace(sentence, rf"{word}\b", "")
        else:
            sentence = tf.strings.regex_replace(sentence, rf"\b{word}\b", "")
    sentence = tf.strings.regex_replace(sentence, r'[!"#$%&()\*\+,-\./:;<=>?@\[\\\]^_`{|}~\']', "")

    return sentence

maxLeght = 30
def fitVectorizer(trainSentences, standardizeFunc):
    vectorizer = tf.keras.layers.TextVectorization(
    standardize=standardizeFunc,
    output_sequence_length=maxLeght
    )

    vectorizer.adapt(trainSentences)
    return vectorizer

vectorizer = fitVectorizer(patterns, standardizeFunc)
vocabSize = vectorizer.vocabulary_size()

labelEncoder = tf.keras.layers.StringLookup(num_oov_indices=0)
labelEncoder.set_vocabulary(labelVocab)

#Model Chatbot
def getResponse(predictTag, confidence):
    if confidence < 0.55:
        return "Maaf, input yang Anda masukkan kurang jelas."
    return np.random.choice(responses[predictTag])

def showRespon(input):
    stemming = stemmer.stem(input)
    inputA = vectorizer([stemming])
    prediction = modelChatbot.predict(inputA)

    predictClassIndex = np.argmax(prediction, axis=-1)
    confidence = prediction[0][predictClassIndex[0]]
    predictTag = labelEncoder.get_vocabulary()[predictClassIndex[0]]
    response = getResponse(predictTag, confidence)

    return response

#Model Classification
def predictAsamlambung(input_data):
    if isinstance(input_data, dict):
        input_data = pd.DataFrame([input_data])  
    
    prediction = modelClass.predict(input_data)
    predictedPercentage = prediction[0][0]
    
    if predictedPercentage >= 0.5:
        predictedlabel = "Ya"
        lifestyle = (
            "Anda terdeteksi terkena Asam Lambung, anda dapat berkonsultasi lebih lanjut melalui Chatbot!\n"
            "Terapkan Pola Hidup Sehat berikut ini untuk meredakan gejala Asam Lambung:\n\n"

            "1. Menurunkan Berat Badan\n"
            "Obesitas adalah salah satu penyebab utama GERD. Lemak perut berlebih memberikan tekanan pada lambung, sehingga mendorong asam lambung naik ke kerongkongan.\n"
            "- Terapkan pola makan sehat dengan rendah lemak dan kalori.\n"
            "- Rutin berolahraga untuk menjaga berat badan ideal.\n\n"
            
            "2. Hindari Makanan Pemicu Refluks\n"
            "Banyak sekali makanan yang mudah memicu refluks asam. Jika kamu mengidap penyakit asam lambung, sebaiknya hindari jenis makanan berikut:\n"
            "- Makanan berlemak dan pedas.\n"
            "- Makanan asam (jeruk, tomat).\n"
            "- Minuman berkafein atau berkarbonasi (kopi, soda).\n"
            "- Cokelat, daun mint, dan bawang.\n"
            "Hindari atau kurangi konsumsi makanan tersebut untuk mencegah gejala memburuk.\n\n"

            "3. Makan dalam Porsi Kecil\n"
            "Makan dalam porsi besar dapat memberikan tekanan lebih pada otot kerongkongan bawah dan memicu refluks asam. Jadi, makanlah dalam porsi kecil.\n\n"

            "4. Jangan Berbaring Setelah Makan\n"
            "Berbaring setelah makan sering menjadi kebiasaan yang banyak orang tidak sadari. Padahal, kebiasaan yang satu ini menjadi salah satu penyebab utama naiknya asam lambung.\n"
            "Tunggu minimal 2–3 jam sebelum berbaring atau tidur.\n\n"

            "5. Jangan Merokok\n"
            "Beberapa penelitian menemukan bahwa nikotin dapat mengendurkan otot-otot kerongkongan bagian bawah dan juga dapat mengganggu kemampuan air liur untuk membersihkan asam dari kerongkongan.\n\n"

            "6. Hindari Alkohol\n"
            "Alkohol dapat meningkatkan produksi asam lambung dan mengiritasi kerongkongan. Batasi atau hindari konsumsi alkohol untuk mengelola gejala.\n\n"

            "7. Perhatikan Obat yang Dikonsumsi\n"
            "Beberapa obat ini mampu melemaskan otot kerongkongan bagian bawah, mengganggu proses pencernaan, atau mengiritasi kerongkongan yang sudah meradang. Obat-obatan ini seperti:\n"
            "- Obat antiinflamasi non-steroid.\n"
            "- Penghambat saluran kalsium untuk hipertensi.\n"
            "- Obat asma tertentu, antibiotik, atau zat besi.\n"
            "Jika sedang mengonsumsi obat ini, konsultasikan dengan dokter agar lebih aman.\n\n"

            "8. Kenakan Pakaian Longgar\n"
            "Hindari pakaian ketat atau ikat pinggang yang menekan perut karena dapat memicu naiknya asam lambung."
        )
    else:
        predictedlabel = "Tidak"
        lifestyle = (
            "Anda terdeteksi tidak terkena Asam Lambung, jika ada pertanyaan lebih lanjut, silahkan konsultasikan melalui Chatbot!\n"
            "Untuk Selalu terhindar dari Asam Lambung, Anda dapat menjaga lambung agar tetap sehat dengan menerapkan pola hidup sehat berikut ini:\n\n"
            
            "1. Cukupi Asupan Cairan\n"
            "Minumlah air putih dalam jumlah yang cukup setiap hari untuk membantu proses pencernaan dan menjaga lambung tetap sehat. Disarankan mengonsumsi 6-8 gelas air putih setiap hari.\n\n"
            
            "2. Makan Tepat Waktu\n"
            "Makanlah tepat waktu agar lambung senantiasa sehat. Makan dengan jadwal yang tidak teratur, bisa meningkatkan refluks asam lambung, hingga memicu gejala maag pada pengidapnya. Oleh sebab itu, konsumsi makanan dan camilan sehat secara teratur. Lalu, usahakan untuk duduk saat makan, dan makan pada waktu yang sama setiap harinya.\n\n"
            
            "3. Sempatkan waktu untuk berolahraga\n"
            "Olahraga tidak hanya dapat memperkuat tulang dan otot-otot tubuh saja. Aktivitas fisik yang teratur juga bisa menjaga makanan tetap bergerak melalui sistem pencernaan, sehingga dapat mencegah sembelit serta masalah pencernaan lainnya. Di samping itu, olahraga juga bisa mempertahankan berat badan yang ideal yang baik untuk sistem pencernaan.\n\n"
            
            "4. Hindari Asupan Alkohol dan Rokok\n"
            "Hindari kebiasaan mengonsumsi alkohol dan merokok. Dari kedua kebiasaan ini bisa mengganggu fungsi lambung dan sistem pencernaan secara keseluruhan.\n\n"
            
            "5. Kurangi Makanan Berlemak\n"
            "Makanan berlemak dapat memperlambat proses pencernaan dan meningkatkan risiko gangguan lambung. Pilih makanan dengan kandungan lemak sehat seperti alpukat atau kacang-kacangan, tetapi tetap dalam porsi yang moderat.\n\n"
            
            "6. Makan makanan yang mengandung Serat\n"
            "Serat berperan penting dalam menjaga kesehatan saluran pencernaan, termasuk kesehatan lambung. Kamu bisa memperoleh asupan serat dari buah-buahan, sayuran, kacang-kacangan, dan biji-bijian.\n\n"
            
            "7. Hindari Konsumsi Makanan Pedas dan Asam Berlebihan\n"
            "Walaupun makanan pedas dan asam menggugah selera, konsumsinya secara berlebihan dapat mengiritasi lambung. Batasi konsumsi makanan ini untuk menjaga keseimbangan sistem pencernaan.\n\n"
            
            "8. Porsi Makan yang Seimbang\n"
            "Hindari makan berlebihan dalam satu waktu. Lebih baik makan dengan porsi kecil namun sering, sehingga lambung tidak bekerja terlalu keras."
        )
    
    return predictedlabel, lifestyle


def userInput(umur,bb,rokok,polaMakan,makanPedas,minumSoda,tidurMakan,olahraga,stres,obat,makanLemak,minumKopi,minumAlkohol,
            mual,perutKembung,sakitTenggorokan,kesulitanMenelan,tidakAda,sendawa,panasDada,nyeriPerut):
    inputData = {
        'umur': umur,
        'bb': bb, #Berat Badan
        'merokok': rokok, #Tidak = 0, Iya = 1
        'polaMakan': polaMakan, #Tidak = 0, Iya =1
        'makanPedas': makanPedas, #Skala 1 - 5
        'minumSoda': minumSoda, #Skala 1 - 5
        'tidurSetelahMakan': tidurMakan, #Skala 1 - 5
        'olahraga': olahraga, #Skala 1 - 5
        'strees': stres, #Skala 1 - 5
        'obatObatan': obat, #Skala 1 - 5
        'makanLemak': makanLemak, #Skala 1 - 5
        'minumKopi': minumKopi, #Skala 1 - 5
        'minumAlkohol':minumAlkohol, #Skala 1 - 5
        'Merasa mual jika Stress': mual, #Ada = 1, Tidak Ada = 0
        'Perut kembung': perutKembung, #Ada = 1, Tidak Ada = 0
        'Sakit tenggorokan atau suara serak': sakitTenggorokan, #Ada = 1, Tidak Ada = 0
        'Kesulitan menelan makanan': kesulitanMenelan, #Ada = 1, Tidak Ada = 0
        'Tidak ada':tidakAda, #Ada = 1, Tidak Ada = 0
        'Sering Bersendawa':sendawa, #Ada = 1, Tidak Ada = 0
        'Rasa panas di dada setelah makan atau saat berbaring':panasDada, #Ada = 1, Tidak Ada = 0
        'Nyeri di bagian atas perut atau dada':nyeriPerut #Ada = 1, Tidak Ada = 0
    }
    predictedlabel, lifestyle = predictAsamlambung(inputData)

    return  predictedlabel, lifestyle


#API Chatbot
@app.post("/show_respon/")
async def get_show_respon(input: str):
    response = showRespon(input)
    return {"response": response}

#API Classificaton
@app.post("/user_input/")
async def get_user_input(umur: int, bb: int, rokok: int, polaMakan: int, makanPedas: int, minumSoda: int,
                          tidurMakan: int, olahraga: int, stres: int, obat: int, makanLemak: int, minumKopi: int,
                          minumAlkohol: int, mual: int, perutKembung: int, sakitTenggorokan: int, kesulitanMenelan: int,
                          tidakAda: int, sendawa: int, panasDada: int, nyeriPerut: int):
    predictedlabel, lifestyle = userInput(umur, bb, rokok, polaMakan, makanPedas, minumSoda, tidurMakan, olahraga, stres, obat,
                               makanLemak, minumKopi, minumAlkohol, mual, perutKembung, sakitTenggorokan, kesulitanMenelan,
                               tidakAda, sendawa, panasDada, nyeriPerut)
    return {
        "predicted_label": predictedlabel,
        "lifestyle": lifestyle
    }
