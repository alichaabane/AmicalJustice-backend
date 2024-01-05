docker rmi justice-backend:v1
ENV_FILE=./.env.prod docker-compose up -d  --force-recreate
#ENV_FILE=./.env.prod docker-compose up -d --build
