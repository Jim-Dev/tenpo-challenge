Initializr used to scaffold this springboot project.
JAVA 21 choosen as per challenge requirements.
springboot 3.5
alpine choosen as runtime base image for a slim image
docker compose starts the web app and a postgres db with mapped port 5435 on host to internal 5432 (default postgres port)
An agentic review session was performed at this point to validate the current state against the challenge spec, one potential concern was raised, alpine images don't include timezone data by default and it could cause issues down the line. manually validating the challenge, no timezone related features or calculations were found (for logging, request timestamps will be stored as UTC Epoch data and served with preconfigured Timezone).
Implemented OPENAPI spec documentation
Caffeine for cache (30min as per spec)
Retries (3) with exponential wait time
Validation for requests
Updated diagram