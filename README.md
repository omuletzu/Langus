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
- **Notifications**: For this feature, you have to select **2 languages**, the **interval** you want to receive this notifications (modify by pressing **+ or -**), **common** or **advanced** words or **related words** (English), the turn it **ON**.
- **Dictionary**: Just type the word you want to know or press **Get a random word**. Also for **Guess the word**, type the word then press **Verify**. If it is correct your word counter for that leaderboard will be incremented and if you can't get the word press **Get more info** to see what you had to guess.
- **Logout and close**: by pressing this button from the menu, you will be **logged out** and **Langus** will close itself.
