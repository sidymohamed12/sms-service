# 📡 SMS Service – Module Spring Boot (Twilio, Infobip, Failover)

## 📌 Description

Ce module fournit une **solution complète et modulaire** pour l'envoi de SMS dans une architecture microservices.
Il prend en charge **plusieurs fournisseurs** (Twilio, Infobip, Orange Sénégal) et inclut un **système de failover automatique**, garantissant **haute disponibilité et fiabilité**.

---

## 🚀 Objectifs

- Centraliser l’envoi de SMS via un **service unique**.
- Permettre l’utilisation de **plusieurs providers**.
- Garantir une **résilience élevée** grâce au **Failover**.
- Fournir des **API REST claires et documentées**.
- Intégration facile dans des microservices ou ERP existants.

---

## 🧩 Fonctionnalités principales

### ✅ Envoi de SMS via plusieurs providers

- **Twilio**
- **Infobip**
- Extensible : ajouter un provider = ajouter une classe implémentant `ISmsService`.

### ✅ Failover intelligent

- Liste de providers définie dans `application.yml`.
- Tentative dans l'ordre : Twilio → Infobip → ...
- Basculer automatiquement si le provider principal échoue.

### ✅ API REST simple

- Endpoints pour envoyer des SMS.
- Réponses standardisées avec `success`, `messageId` et `error`.

### ✅ Gestion des erreurs

- Exceptions centralisées via `SmsExceptionHandler`.
- Messages d’erreur clairs pour faciliter le debug.

---

## 🏗 Architecture

```
sms-service/
├── src/main/java/com/sidy/sms_service/
│   ├── config/
│   │   ├── SmsConfiguration.java
│   │   └── SmsProperties.java
│   ├── controller/
│   │   └── SmsController.java
│   ├── dto/
│   │   ├── SmsRequest.java
│   │   └── SmsResponse.java
│   ├── exception/
│   │   ├── SmsConfigurationException.java
│   │   ├── SmsException.java
│   │   ├── SmsExceptionHandler.java
│   │   ├── SmsProviderException.java
│   │   └── SmsServiceException.java
│   └── service/
│       ├── ISmsService.java
│       └── impl/
│           ├── FailoverSmsService.java
│           ├── InfobipSmsService.java
│           └── TwilioSmsService.java
├── src/main/resources/
│   └── application.yml
│── pom.xml
└── .env
```

- **ISmsService** → Interface centrale.
- **TwilioSmsService / InfobipSmsService** → Implémentations concrètes.
- **FailoverSmsService** → Gère l’ordre dynamique des providers.
- **SmsController** → API REST.
- **SmsExceptionHandler** → Centralise les erreurs.
- **SmsProperties** → Mappe les propriétés `sms.*` du YAML.
- **SmsConfiguration** → Déclare le bean principal `ISmsService`.

---

## 🛠 Technologie

| Technologie           | Rôle                     |
| --------------------- | ------------------------ |
| **Spring Boot 3**     | Framework principal      |
| **Spring Web**        | API REST                 |
| **Spring Validation** | Validation des DTO       |
| **Lombok**            | Réduction du boilerplate |
| **Twilio SDK**        | Envoi SMS via Twilio     |
| **Infobip REST SDK**  | Envoi SMS via Infobip    |
| **Java 21+**          | Compatibilité            |

---

## ⚙ Installation des dépendances

**Twilio :**

```xml
<dependency>
  <groupId>com.twilio.sdk</groupId>
  <artifactId>twilio</artifactId>
  <version>8.33.0</version>
</dependency>
```

**Infobip :**

```xml
<dependency>
  <groupId>com.infobip</groupId>
  <artifactId>infobip-api-java-client</artifactId>
  <version>1.1.0</version>
</dependency>
```

**Unirest (HTTP client pour Infobip) :**

```xml
<dependency>
    <groupId>com.konghq</groupId>
    <artifactId>unirest-java</artifactId>
    <version>3.13.6</version>
</dependency>
```

---

## 🛠 Configuration `application.yml`

```yaml
sms:
  provider: failover
  failoverProviders:
    - twilio
    - infobip

  twilio:
    accountSid: ${TWILIO_ACCOUNT_SID}
    authToken: ${TWILIO_AUTH_TOKEN}
    fromNumber: ${TWILIO_PHONE_NUMBER}

  infobip:
    baseUrl: https://d8lmp8.api.infobip.com
    apiKey: ${INFOBIP_API_KEY}
    fromNumber: ${INFOBIP_SENDER}
```

### 🔹 Exemple `.env` (à placer à la racine du module)

```env
# Twilio
TWILIO_ACCOUNT_SID=your_twilio_account_sid
TWILIO_AUTH_TOKEN=your_twilio_auth_token
TWILIO_PHONE_NUMBER=+1234567890

# Infobip
INFOBIP_API_KEY=your_infobip_api_key
INFOBIP_SENDER=+0987654321
```

> ⚠️ Les valeurs sensibles **ne doivent pas être commitées**. Copier `.env.example` → `.env` et remplir vos propres secrets.

---

## 🧠 Principes de conception

- **Single Responsibility Principle (SRP)** → Chaque classe a un rôle unique.
- **Open/Closed Principle (OCP)** → Ajouter un provider sans modifier les classes existantes.
- **Dependency Injection** → Testabilité et modularité améliorées.
- **Failover Pattern** → Haute disponibilité.
- **Testabilité** → Les providers implémentent `ISmsService` → facilement mockables.

---

## 🧪 Tests

- Tester le failover en simulant une panne d’un provider.
- Mock des services Twilio et Infobip.
- Tester l’API REST via Postman ou cURL :

```bash
curl -X POST http://localhost:8080/api/sms/send \
-H "Content-Type: application/json" \
-d '{"to":"+221761823698","message":"Test SMS"}'
```

---

## 👤 Auteur

**Sidy Mohamed Saizonou – Développeur Fullstack**
✨ Passionné par les architectures modulaires, microservices et DevOps.

---

## 📌 Version & Support

| Élément              | Valeur                          |
| -------------------- | ------------------------------- |
| Version module       | 1.0.1                           |
| Compatibilité Spring | 3.x                             |
| JDK                  | 17+                             |
| Support              | Créer une issue ou me contacter |
