docker rmi justice-backend:v1
ENV_FILE=./.env.prod docker-compose up -d --build
