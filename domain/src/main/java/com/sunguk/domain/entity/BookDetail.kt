package com.sunguk.domain.entity


data class BookDetail(
    val title: String,                  // "Securing DevOps"
    val subtitle: String,               // "Security in the Cloud"
    val authors: String,                // "Julien Vehent"
    val publisher: String,              // "Manning"
    val language: String,               // "English"
    val isbn10: String,                 // "1617294136"
    val isbn13: String,                 // "9781617294136"
    val pages: String,                  // "384"
    val year: String,                   // "2018"
    val rating: String,                 // "4"
    val desc: String,                   // "An application running in the cloud can benefit from incredible efficiencies, but they come with unique security threats too. A DevOps team's highest priority is understanding those risks and hardening the system against them.Securing DevOps teaches you the essential techniques to secure your c..."
    val price: String,                  // "$39.65"
    val imageUrl: String,               // "https://itbook.store/img/books/9781617294136.png"
    val detailPageUrl: String,          // "https://itbook.store/books/9781617294136"
    val pdfs: List<PdfInfo>,             // {"Chapter 2": "https://itbook.store/files/9781617294136/chapter2.pdf", "Chapter 5": "https://itbook.store/files/9781617294136/chapter5.pdf"}
)
