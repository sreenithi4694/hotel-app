FROM node:20.10.0 AS build
WORKDIR /app
#COPY package*.json /app/
COPY . .
#RUN npm install -g @angular/cli
RUN npm install
RUN npm run build -- --output-path=./dist/out
#COPY ./ /app/
#RUN npm run build -- --output-path=./dist/out
#EXPOSE 4200
#ENTRYPOINT ["npm", "start"]
#CMD ["ng", "serve", "--host", "0.0.0.0"]

FROM nginx:alpine
COPY --from=build /app/dist/out/* /usr/share/nginx/html/
#COPY /nginx.conf /etc/nginx/conf.d/default.conf
COPY /nginx.conf /etc/nginx/nginx.conf

