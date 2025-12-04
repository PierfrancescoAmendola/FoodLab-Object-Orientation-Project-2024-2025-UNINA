# FoodLab Unina 🍳

Un'applicazione Java Swing completa per la gestione di corsi di cucina presso l'Università degli Studi di Napoli. Permette a chef professionisti di creare e gestire corsi culinari, mentre gli studenti possono iscriversi e seguire le lezioni.

## 📋 Descrizione del Progetto

FoodLab Unina è una piattaforma digitale che collega chef esperti con studenti appassionati di cucina. L'applicazione offre:

- **Per i Chef**: Creazione e gestione di corsi, sessioni e ricette
- **Per gli Studenti**: Iscrizione ai corsi, accesso ai materiali didattici
- **Sistema di Report**: Statistiche dettagliate sui corsi e sugli iscritti
- **Interfaccia Intuitiva**: Design ispirato al mondo culinario con tema cucina personalizzato

## 🏗️ Architettura

L'applicazione segue il pattern architetturale **MVC (Model-View-Controller)**:

```
src/
├── boundary/          # View - Interfacce utente (Swing)
├── controller/        # Controller - Logica di business
├── DAO/              # Data Access Objects - Accesso al database
└── entity/           # Model - Modelli di dati
```

### Componenti Principali

- **MainApplication.java**: Gestore principale delle interfacce con CardLayout
- **Controller.java**: Controller centrale con gestione errori avanzata
- **KitchenTheme.java**: Tema UI personalizzato con colori culinari
- **DAO Classes**: Gestione persistenza dati PostgreSQL
- **Entity Classes**: Modelli di dominio (Chef, Corso, Studente, etc.)

## 🚀 Caratteristiche

### 👨‍🍳 Area Chef
- Registrazione e login sicuro
- Creazione di nuovi corsi culinari
- Gestione sessioni e ricette
- Visualizzazione iscritti ai corsi
- Report mensili con statistiche dettagliate
- Sistema di notifiche

### 👨‍🎓 Area Studente
- Registrazione e login
- Esplorazione corsi disponibili
- Iscrizione ai corsi
- Accesso ai materiali didattici
- Dashboard personalizzata

### 📊 Report e Statistiche
- Grafici sui corsi più popolari
- Statistiche sugli iscritti
- Report mensili dettagliati
- Visualizzazioni native (senza dipendenze esterne)

## 🛠️ Tecnologie Utilizzate

- **Java**: Linguaggio di programmazione principale
- **Swing**: Framework per l'interfaccia utente
- **PostgreSQL**: Database relazionale
- **JDBC**: Driver per connessione al database
- **MigLayout**: Layout manager per componenti UI
- **Git**: Controllo versione
- **VS Code**: Ambiente di sviluppo

## 📋 Prerequisiti

### Sistema Operativo
- macOS (consigliato)
- Windows/Linux (compatibile)

### Software Richiesto
- **Java JDK 11+** (consigliato JDK 17 o 21)
- **PostgreSQL 12+** con database `FoodLab2025`
- **Git** per il controllo versione

### Dipendenze Java
Le seguenti librerie sono incluse nel progetto:
- `postgresql-42.7.7.jar` - Driver JDBC PostgreSQL
- `com.miglayout.core_11.4.2.jar` - MigLayout Core
- `com.miglayout.swing_11.4.2.jar` - MigLayout Swing

## ⚙️ Installazione e Setup

### 1. Clonare il Repository
```bash
git clone https://github.com/PierfrancescoAmendola/FoodLab-Object-Orientation-Project-2024-2025-UNINA.git
cd FoodLab2025
```

### 2. Configurazione Database PostgreSQL

#### Creare il Database
```sql
-- Connettiti come superuser PostgreSQL
createdb FoodLab2025
```

#### Configurare l'Utente Database
```sql
-- Crea l'utente postgres (se non esiste)
createuser -s postgres

-- Imposta la password
psql -c "ALTER USER postgres PASSWORD 'a';"
```

#### Verifica Connessione
```bash
# Test connessione
psql -h localhost -U postgres -d FoodLab2025
```

### 3. Compilazione del Progetto

#### Usando VS Code (Raccomandato)
1. Apri il progetto in VS Code
2. Premi `Ctrl+Shift+P` (o `Cmd+Shift+P` su Mac)
3. Seleziona "Tasks: Run Task"
4. Scegli "Compile FoodLab"

#### Compilazione Manuale
```bash
# Crea directory bin se non esiste
mkdir -p bin

# Compila tutti i file sorgente
javac -cp ".:src/jdbc/postgresql-42.7.7.jar:bin" \
      -d bin \
      src/controller/Controller.java \
      src/DAO/*.java \
      src/boundary/*.java \
      src/entity/*.java
```

### 4. Esecuzione dell'Applicazione

#### Usando VS Code
1. Premi `F5` o vai a Run & Debug
2. Seleziona "Run FoodLab Controller"
3. Premi Play

#### Esecuzione Manuale
```bash
# Esegui l'applicazione
java -cp "bin:src/jdbc/postgresql-42.7.7.jar" controller.Controller
```

## 📊 Struttura Database

L'applicazione utilizza le seguenti tabelle principali:

- **Utente**: Informazioni utenti (chef e studenti)
- **Corso**: Corsi culinari creati dai chef
- **Sessione**: Sessioni specifiche dei corsi
- **Ricetta**: Ricette associate alle sessioni
- **Iscrizione**: Iscrizioni studenti ai corsi
- **Avviso**: Notifiche e annunci
- **ReportMensile**: Statistiche mensili

### Schema Database Suggerito
```sql
-- Tabella principale utenti
CREATE TABLE Utente (
    codice_fiscale VARCHAR(16) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    telefono VARCHAR(15),
    data_registrazione DATE DEFAULT CURRENT_DATE,
    chef BOOLEAN DEFAULT FALSE
);

-- Altri schemi disponibili nei file DAO
```

## 🎨 Tema UI

L'applicazione utilizza un tema personalizzato ispirato al mondo culinario:

- **Colori principali**: Arancione caldo, rosso pomodoro, crema, marrone legno
- **Font**: Georgia per titoli, Arial per testi
- **Stile**: Bordi arrotondati, ombre, gradienti
- **Icone**: Emoji fallback per icone mancanti

## 🔧 Risoluzione Problemi

### Errore: "role postgres does not exist"
```bash
# Crea l'utente postgres
createuser -s postgres
psql -c "ALTER USER postgres PASSWORD 'a';"
```

### Errore: "Connection refused"
```bash
# Avvia PostgreSQL
brew services start postgresql  # macOS con Homebrew
sudo systemctl start postgresql # Linux
# Windows: Avvia il servizio PostgreSQL
```

### Errore: "Database FoodLab2025 does not exist"
```bash
createdb FoodLab2025
```

### Errore: "Driver JDBC non trovato"
- Verifica che `postgresql-42.7.7.jar` sia presente in `src/jdbc/`
- Ricompila con il classpath corretto

### Scrolling non funziona
- L'applicazione include utility personalizzate per il mouse wheel scrolling
- Verifica che i pannelli utilizzino `ScrollHelper`

## 📈 Statistiche Progetto

- **Linee di codice**: ~10,532
- **File Java**: 35
- **Classi**: 14 boundary, 11 DAO, 10 entity, 1 controller
- **Commit**: 50+
- **Dipendenze esterne**: 4 librerie

## 🤝 Contributi

Il progetto è sviluppato come lavoro universitario presso l'Università degli Studi di Napoli.

### Come contribuire
1. Fork il repository
2. Crea un branch per la tua feature (`git checkout -b feature/nuova-feature`)
3. Commit delle modifiche (`git commit -am 'Aggiungi nuova feature'`)
4. Push del branch (`git push origin feature/nuova-feature`)
5. Apri una Pull Request

## 📝 Licenza

Questo progetto è distribuito sotto licenza MIT. Vedere il file `LICENSE` per maggiori dettagli.

## 👥 Autori

- **Sviluppato da**: Studenti dell'Università degli Studi di Napoli. Pierfrancesco Amendola, Salvatore Correra, Umberto De Benedictis
- **Repository**: https://github.com/PierfrancescoAmendola/FoodLab-Object-Orientation-Project-2024-2025-UNINA

## 📞 Supporto

Per supporto tecnico o domande:
- Apri una issue su GitHub
- Contatta il team di sviluppo

---

**🍳 Buon coding culinario!**