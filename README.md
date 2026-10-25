"# siwcatering23" 

## Configurazione

Le credenziali non sono nel codice: src/main/resources/application.properties le legge da variabili d'ambiente.
Prima di avviare l'applicazione imposta:

- `DB_PASSWORD`: password dell'utente PostgreSQL indicato in `spring.datasource.username`

Esempio (PowerShell):

```powershell
$env:DB_PASSWORD = "..."
.\mvnw spring-boot:run
```
