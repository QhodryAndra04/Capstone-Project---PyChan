# Capstone-Project---PyChan
PhyChan : Consultation friend for your health problems  

---
## Running Machine Learning(Preprocessing & Model)

### Download Directory "modelPhychan"

### Setup Environment - Shell/Terminal
open Shell/Terminal  
cd modelPhychan(if not already in the directory)  
pip install pipenv  
pipenv shell  
pip install -r requirements.txt  

### File run sequence
dataPreprocess  
classification  
chatbot  
main  

### Alternative run on Google Colab
https://colab.research.google.com/drive/1-FE9cVuBtWb7Q2gunHZrdtUp431wow4m?usp=sharing

---

# Cloud Computing - Deploying in Cloud Run

This guide provides a step-by-step process for deploying your application on **Google Cloud Run**.

---

## Prerequisites

Make sure you have the following:
1. **Google Cloud Account**: Ensure you have an active account.
2. **gcloud CLI**: Install and configure the [gcloud CLI](https://cloud.google.com/sdk/docs/install).
3. **Project Setup**: Note your:
   - `PROJECT_ID`: The ID of your Google Cloud project.
   - `PROJECT_NUMBER`: The project number.

---

## Deployment Steps

### 1. Open Google Cloud Console
Navigate to the [Google Cloud Console](https://console.cloud.google.com/).

### 2. Open Terminal
Access the terminal in the console or locally on your machine.

### 3. Navigate to Your Backend Directory
Run the following command:
```bash
cd backend
```

### 4. Authenticate with Google Cloud
Run the following command:
```bash
gcloud auth login
```

### 5.  Set the Default Project
```bash
gcloud config set project PROJECT_ID
```

### 6.  Enable Required APIs
```bash
gcloud services enable run.googleapis.com \
    cloudbuild.googleapis.com
```

### 7. Grant Permissions for Cloud Build
```bash
gcloud projects add-iam-policy-binding PROJECT_ID \
    --member=serviceAccount:PROJECT_NUMBER-compute@developer.gserviceaccount.com \
    --role=roles/cloudbuild.builds.builder
```

### 8. Deploy the Application
```bash
gcloud run deploy --source .
```

# Panduan Penggunaan Firestore di Google Cloud

Firestore adalah layanan database NoSQL berbasis cloud yang mudah digunakan untuk membangun aplikasi yang scalable dan real-time. Berikut adalah langkah-langkah untuk memulai:

---

## 1. Persiapan Awal

1. **Buat atau Masuk ke Akun Google Cloud**
   - Akses [Google Cloud Console](https://console.cloud.google.com/).
   - Login menggunakan akun Google Anda.

2. **Buat Proyek Baru**
   - Klik tombol **"Create Project"**.
   - Isi nama proyek, pilih organisasi (opsional), lalu klik **"Create"**.

3. **Aktifkan Firestore**
   - Di Google Cloud Console, buka **Menu** → **Firestore**.
   - Pilih opsi **"Create Database"**.

---

## 2. Konfigurasi Database Firestore

1. **Pilih Mode Database**
   - **Native Mode**: Untuk aplikasi baru dan lebih banyak fitur.
   - **Datastore Mode**: Jika sudah menggunakan Google Datastore.
   - Pilih mode sesuai kebutuhan Anda dan klik **"Next"**.

2. **Atur Lokasi**
   - Pilih lokasi geografis untuk menyimpan data.
   - Klik **"Enable"** untuk mengaktifkan Firestore.

---

## 3. Mengintegrasikan Firestore ke Proyek Anda

### a. **Menambahkan Firebase SDK**
1. **Pilih SDK Sesuai Platform**
   - Untuk **Node.js**, **Python**, **Java**, atau **Android**.

2. **Instal Firebase SDK**
   - Misalnya, untuk Node.js:
     ```bash
     npm install firebase
     ```

3. **Konfigurasikan Firebase**
   - Unduh file konfigurasi dari Firebase Console.
   - Gunakan kode berikut untuk inisialisasi:
     ```javascript
     const { initializeApp } = require("firebase/app");
     const { getFirestore } = require("firebase/firestore");

     const firebaseConfig = {
       apiKey: "YOUR_API_KEY",
       authDomain: "YOUR_AUTH_DOMAIN",
       projectId: "YOUR_PROJECT_ID",
       storageBucket: "YOUR_STORAGE_BUCKET",
       messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
       appId: "YOUR_APP_ID"
     };

     const app = initializeApp(firebaseConfig);
     const db = getFirestore(app);
     ```

---

### b. **Menulis dan Membaca Data**

#### **Menambahkan Dokumen**
Gunakan metode `add` atau `set`:
```javascript
const { collection, addDoc } = require("firebase/firestore");

async function addData(db) {
  try {
    const docRef = await addDoc(collection(db, "users"), {
      name: "John Doe",
      email: "john@example.com",
      age: 25
    });
    console.log("Document written with ID: ", docRef.id);
  } catch (e) {
    console.error("Error adding document: ", e);
  }
}

