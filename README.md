<p align="center">
  <img src="https://i.ibb.co.com/9c7hPzC/Logo.png" width="200" />
</p>

<h1 align="center"> PhyChan</h1>

<h2 align="center"> Consultation friend for your health problems </h2>

<p align="center">
  This is a project to fulfill the Bangkit Academy led by Google, Tokopedia, Gojek, & Traveloka Program.
</p>

<p align="center">
  © C242 - PS211 Bangkit Capstone Team
</p>

# Mobile Development
---

## Features
---
* Splash Screen
* Email Authentication
* Prediction stomach acid
* Dashboard detail stomach acid
* Healthy stomach
* Chatbot
* Alarm
* Profile
* Change password
* Repository GitHub

## Built With
---
* Kotlin
* Material Components
* JUnit
* Espresso Core
* Retrofit
* Gson
* Glide

## Package Structure
---
```bash
.com.example.pychan
├── api
├── data
├── retrofit
├── ui
│   ├── activity
│   ├── adapter
│   ├── fragment
│   └── receiver
```

# Running Machine Learning (Preprocessing & Model)
---

## Download Directory "modelPhychan"
---

## Setup Environment - Shell/Terminal
```bash
open Shell/Terminal  
cd modelPhychan(if not already in the directory)  
pip install pipenv  
pipenv shell  
pip install -r requirements.txt  
```

---
## File run sequence
- dataPreprocess  
- classification  
- chatbot  
- main  

---
## Alternative run on Google Colab
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

## Guide to Using Firestore on Google Cloud

Firestore is an easy-to-use cloud-based NoSQL database service for building scalable, real-time applications. Here are the steps to get started:

---

### 1. Initial Setup

1. **Create or Sign in to a Google Cloud Account**
   - Access the [Google Cloud Console](https://console.cloud.google.com/).
   - Sign in using your Google account.

2. **Create a New Project**
   - Click the **"Create Project"** button.
   - Fill in the project name, select an organization (optional), and then click **"Create"**.

3. **Enable Firestore**
   - In the Google Cloud Console, go to **Menu** → **Firestore**.
   - Select the **"Create Database"** option.

---

### 2. Configure the Firestore Database

1. **Select Database Mode**
   - **Native Mode**: For new applications and more features.
   - **Datastore Mode**: If you are already using Google Datastore.
   - Select the mode according to your needs and click **"Next"**.

2. **Set Location**
   - Select the geographic location to store the data.
   - Click **"Enable"** to enable Firestore.

---

### 3. Integrate Firestore into Your Project

#### a. **Adding Firebase SDK**
1. **Choose SDK According to Platform**
   - For **Node.js**, **Python**, **Java**, or **Android**.

2. **Install Firebase SDK**
   - For example, for Node.js:
     ```bash
     npm install firebase
     ```

3. **Configure Firebase**
   - Download the configuration file from the Firebase Console.
   - Use the following code for initialization:
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

#### b. **Writing and Reading Data**

##### **Adding Documents**
Use the `add` or `set` method:
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
```

### 4. Managing Firestore Data via Google Cloud Console
Use the Firestore tab in the Cloud Console to monitor collections, documents, and quota usage.

### 5. Adding Security with Firestore Rules
#### a. Open the Rules tab in the Firestore Console
#### b. Add security rules, for example:
```bash
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null;
    }
  }
}
```
#### c. Save changes

### 6. Monitoring and Optimization
#### a. Use Cloud Monitoring to monitor Firestore performance
#### b. Enable logging to track queries
