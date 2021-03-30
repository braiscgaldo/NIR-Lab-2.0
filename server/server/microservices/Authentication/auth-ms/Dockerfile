# syntax = docker/dockerfile:experimental
FROM node:12.19.0

# SET ENVIROMENT VARS
ENV NODE_ENV production
ENV APP_ROOT /src/
ENV HOST https://localhost/auth
ENV DEBUG "app:*"
ENV PORT 3000
ENV SENTRY_ENV production

# GET SSH PUB
RUN mkdir -p -m 0600 ~/.ssh && ssh-keyscan git.ggtech.global >> ~/.ssh/known_hosts

# CREATE PATH TO WORK
RUN mkdir ${APP_ROOT}
WORKDIR ${APP_ROOT}

ADD . ${APP_ROOT}

# INSTALL AND BUILD
RUN --mount=type=ssh yarn install --production=false
RUN yarn build

# EXPOSE 3000

CMD ["yarn", "start"]
