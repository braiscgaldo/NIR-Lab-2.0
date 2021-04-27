FROM node:12.9.1 as builder

# hacer la carpeta 'app' el directorio de trabajo actual
WORKDIR /app

# copiar 'package.json' y 'package-lock.json' (si están disponibles)
COPY View.tar.gz ./

# unzip project
RUN tar -xvf View.tar.gz

# instalar dependencias del proyecto
WORKDIR /app/View/nir-lab/
RUN rm -r node_modules
RUN npm install -g @vue/cli
RUN npm install -g @vue/cli-service@4.5.10
RUN npm install bootstrap-vue
RUN npm install vuetify
RUN npm install vue-table-dynamic
RUN npm install vee-validate
RUN npm install -g @vue/compiler-sfc@^3.0.0-beta.14
RUN npm install -g sass sass-loader
RUN npm install --save vue-axios
RUN npm install -g vuex-persistedstate
RUN npm install 

WORKDIR /app
RUN rm View.tar.gz

# construir aplicación para producción minificada
WORKDIR /app/View/nir-lab

RUN npm run build


FROM nginx:alpine as production-build
COPY ./nginx.conf /etc/nginx/nginx.conf

## Remove default nginx index page
RUN rm -rf /usr/share/nginx/html/*

# Copy from the stahg 1
COPY --from=builder /app/View/nir-lab/dist /usr/share/nginx/html

EXPOSE 8080
ENTRYPOINT ["nginx", "-g", "daemon off;"]

