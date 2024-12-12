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

## **Cloud Computing**

### **Deploy in Cloud Run**
Open Cloud Console
Open terminal
Cd backend
type in terminal "gcloud auth login"
To set the default project for your Cloud Run service "gcloud config set project PROJECT_ID"
Enable the Cloud Run Admin API and the Cloud Build API "gcloud services enable run.googleapis.com \
    cloudbuild.googleapis.com"
For Cloud Build to be able to build your sources, grant the Cloud Build Service Account role to the Compute Engine default service account by running the following:
"gcloud projects add-iam-policy-binding PROJECT_ID \
    --member=serviceAccount:PROJECT_NUMBER-compute@developer.gserviceaccount.com \
    --role=roles/cloudbuild.builds.builder"
Deploy the current folder using the following command "gcloud run deploy --source ."
