## Langus
  Langus is a language learning app designed to make learning a foreign language both engaging and effective. With a range of features tailored to suit every learner's needs, Langus empowers users to master new languages at their own pace.

  <img src="https://github.com/omuletzu/Langus/assets/75565975/cdecff7f-8b2d-43a3-8b3c-058e56fb2689" alt="Screenshot 1" width="300">

## Features
- **Notification scheduler**: Customize your learning experience with a flexible notification scheduling. Choose 2 languages, to translate the received words from one to another. You can choose between English, French, German, Spanish and Romanian and after that you will be notified at the set time with a pair of translated words (either common or advanced ones). Also you can say a word, and you will be notified with related words (but this is only for English).
- **Detailed dictionary**: There is a built in dictionary, so you can acces it by typing a word and you will get details about it, like definition, synonyms, antonyms, related words and examples. Also you can save that word for later if you want too. (this feature is available only for English). By pressing the **Get a random word** you will receive the details about a **random** word.
- **Guess the word game**: This game was made to test you language skills. Given a definition you need to guess the word. There is a community leaderboard, ordered by guessed words and you can check your rating in **Profile** section.
- **Saved words**: Here is the place where you can keep track of all the words you saved.
- **Community**: In this section you can see all the users (everyone can set a favourite word and why so) and also there is a search feature, where you can search for others.

 ## Implementation overview
- **Technologies used**: This app was made in **Android Studio** and uses **Kotlin** for frontend and **XML** for backend. Also it has **WordNET**, a **lexical database** and it is manipulated by **JWI** library.
- **Data Storage and Management**: User data, artwork information and other relevant data are stored in Firebase, a database hosted by Google.

## Installation 

  In order to install Langus on your phone, you need to get all the files on your machine, that has **Android Studio**, because using this tool, you can build the APK.

- **Step1**: Download all the files, either manually, or in your git bash use `git clone https://github.com/omuletzu/Langus`
- **Step2**: You have to open all this files as a project in **Android Studio** and build the APK, and after this, using an USB cable get the APK on your phone and run the APK

## Usage
- **Sign in or create an account**: First of all you have to either login or create an account.

  
  <img src="https://github.com/omuletzu/Langus/assets/75565975/6c0dd8b4-b78e-492e-94ac-1813b2e4d7b5" alt="Screenshot 1" width="150">
  <img src="https://github.com/omuletzu/Langus/assets/75565975/0a2813ca-706f-42c2-839b-7e717461e781" alt="Screenshot 2" width="150">


- **Notifications**: For this feature, you have to select **2 languages**, the **interval** you want to receive this notifications (modify by pressing **+ or -**), **common** or **advanced** words or **related words** (English), the turn it **ON**.


  <img src="https://github.com/omuletzu/Langus/assets/75565975/1a039d82-ff5e-408f-bb19-b1b52e0a89b3" alt="Screenshot 3" width="150">
  <img src="https://github.com/omuletzu/Langus/assets/75565975/9ae8a841-dd46-46c0-9ea0-cff1a2fc6e72" alt="Screenshot 4" width="150">


- **Dictionary**: Just type the word you want to know or press **Get a random word**. Also for **Guess the word**, type the word then press **Verify**. If it is correct your word counter for that leaderboard will be incremented and if you can't get the word press **Get more info** to see what you had to guess.

  
  <img src="https://github.com/omuletzu/Langus/assets/75565975/908cbad9-0531-4889-ba44-cacfab679337" alt="Screenshot 6" width="150">
  <img src="https://github.com/omuletzu/Langus/assets/75565975/8852bb05-ec4b-42c2-a408-73abfcd3e29e" alt="Screenshot 5" width="150">
  <img src="https://github.com/omuletzu/Langus/assets/75565975/1bb97565-e73b-42b4-8fd6-9eea29868f38" alt="Screenshot 4" width="150">
  

- **Logout and close**: By pressing this button from the menu, you will be **logged out** and **Langus** will close itself.


  <img src="https://github.com/omuletzu/Langus/assets/75565975/c5087883-b1f6-45d1-a16a-f02f976689cf" alt="Screenshot 6" width="150">
  <img src="https://github.com/omuletzu/Langus/assets/75565975/ef82c490-225d-442a-89bf-6873dbc71f65" alt="Screenshot 7" width="150">
  <img src="https://github.com/omuletzu/Langus/assets/75565975/194e70b9-8ae4-42a3-a695-e87c57e10810" alt="Screenshot 8" width="150">
