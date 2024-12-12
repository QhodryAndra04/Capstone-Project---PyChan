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
