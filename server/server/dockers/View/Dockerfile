FROM node:12.9.1

# hacer la carpeta 'app' el directorio de trabajo actual
WORKDIR /app

# copiar 'package.json' y 'package-lock.json' (si están disponibles)
COPY View.zip ./

# unzip project
RUN unzip View.zip

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
RUN npm install -g vuex-persistent-state
RUN npm install 

WORKDIR /app
RUN rm View.zip

# construir aplicación para producción minificada
WORKDIR /app/View/nir-lab

EXPOSE 8080
CMD [ "npm", "run", "serve" ]
