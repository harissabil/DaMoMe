![damome_banner](https://github.com/user-attachments/assets/6013a0f1-0fef-43fe-bd14-dd13183e0da6)

# DaMoMe: Daily Money Manager

DaMoMe is a smart income and expense tracking app built with Kotlin Multiplatform, targeting Android and Desktop. It simplifies financial management by allowing users to scan receipts for quick income or expense recording, avoiding the need for manual input. Additionally, the app features an AI-powered chatbot with Retrieval-Augmented Generation (RAG) support (currently for Android) to provide personalized financial advice and insights tailored to the user’s needs.

The app addresses the issue of low financial literacy, which remains a persistent challenge. For instance, the 2024 P-Fin Index reports that [financial literacy in the US has hovered around 50% for eight consecutive years](https://gflec.org/initiatives/personal-finance-index/), with a 2% drop in the past two years. DaMoMe offers an accessible solution to promote better money management habits. With its intuitive features and advanced AI combination, DaMoMe helps users make smarter financial decisions and stay in control of their finances.

## Table of Contents
- [Demo Video](#demo-video)
- [Features](#features)
- [Architecture](#architecture)
- [About RAG](#about-rag)
- [Library Used](#library-used)
- [Installation](#installation)
- [Future Roadmap](#future-roadmap)
- [License](#license)

## Demo Video
Here’s a demo video showcasing the features of the app.

https://github.com/user-attachments/assets/22ec887f-e48d-4db3-a373-5355095b6d8a

## Features
Here are the 4 main features of the app:
1. Overview - Easily view your financial summary, including total balance, income, and expenses. The data can be filtered by daily, weekly, monthly, or all-time on the Home screen.
2. EasyAdd - Three options to add transactions: manually typing, selecting an image, or capturing a photo (for Android). Android users can also share images directly from the gallery or banking apps to simplify adding transactions.
3. Records - View all transaction history categorized by month and year. Includes a search feature to quickly find specific transactions.
4. DaMommy - A chatbot powered by Gemini API that helps you manage your finances and provides insights and personalized recommendations.

## Architecture
The app follows the Model-View-ViewModel (MVVM) architecture to ensure a clean separation of concerns and maintainable code.

![DaMoMe_architecture](https://github.com/user-attachments/assets/4ddd17c0-4c3e-4bc5-8b04-10ec1aa97222)

## About RAG
One of the weaknesses of Generative AI is its tendency to hallucinate, especially when it lacks relevant context. To address this issue, on the Android platform I combined the Gemini API with a vector database using ObjectBox for vector search. This approach helps provide context to queries and significantly improves accuracy.  

The vector data model is defined as follows:  

```kotlin
@HnswIndex(
    dimensions = 768,
    distanceType = VectorDistanceType.COSINE,
    neighborsPerNode = 30,
    indexingSearchCount = 200
)
override var embedding: FloatArray? = null
```

And the query workflow will be like the following:

![DaMoMe_rag_workflow](https://github.com/user-attachments/assets/2ac80be3-0a73-4326-8e84-4586d9f1f5af)

## Library Used
Here are some of the libraries used to build the app:  

| **Category**           | **Library**                  |  
|-------------------------|------------------------------|  
| UI Library             | [Miuix](https://github.com/miuix-kotlin-multiplatform/miuix) |  
| Navigation             | [Navigation Compose](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation-routing.html) |  
| AI SDK                 | [Google Generative AI SDK](https://github.com/PatilShreyas/generative-ai-kmp) |  
| HTTP Client            | [Ktor](https://ktor.io/) |  
| Database               | [ObjectBox](https://objectbox.io/) and [Room](https://developer.android.com/kotlin/multiplatform/room) |  
| Dependency Injection   | [Koin](https://insert-koin.io/docs/reference/koin-mp/kmp/) |  

## Installation

To build this project, you need the latest stable version of [Android Studio](https://developer.android.com/studio) and [Kotlin Multiplatform Plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform) installed.  

1. Clone or download the project and open it in Android Studio.  
2. Get your [Gemini API key](https://aistudio.google.com/apikey) and add it to the `AppSecret.kt` file located in: `src/commonMain/kotlin/com/harissabil/damome/core/secret` like the following:

   ```kotlin
   package com.harissabil.damome.core.secret  

   object AppSecret {  
       const val GEMINI_API_KEY = "your api key here"  
   }

### Run on Android

3. In the list of run configurations, select `composeApp`.
4. Choose your Android virtual or physical device and then click **Run**.

### Run on Desktop

3. Select **Run | Edit Configurations** from the main menu.
4. Click the plus button and choose **Gradle** from the dropdown list.
5. In the **Tasks and arguments** field, paste this command:
   
   ```bash
   composeApp:run
   
6. Click **OK**.
7. Use this configuration and then click **Run**.

## Future Roadmap  

- [ ] Pocket system (track funds from bank, e-wallet, etc.)  
- [ ] Vector database for desktop (RAG support)  
- [ ] Multi-currency support  
- [ ] Statistics and insights  

## License
DaMoMe is open-source and released under the [MIT License](LICENSE).
