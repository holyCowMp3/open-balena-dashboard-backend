# OpenBalena backend for Dashboard
#### TL;DR
Dockerized back-end proxy for [OpenBalena](https://www.balena.io/open/) project.
Front-end from [Razikus/open-balena-dashboard](https://github.com/Razikus/open-balena-dashboard)
## Usage 
Main purpose of this project is proxying requests to open-balena-api without the TLS issues from self-signed CA.
You can use it with, or without front-end part, which are included in this solution.
After setting property with open-balena-api instance - use URL of deployed proxy as link for api.
For example:
```
API address - api.example.com
Dashboard address - dashboard.example.com

Email - email from open balena instance
Password - your password
Link - dashboard address etcetera - dashboard.example.com (NOT api.example.com!)

```
## Example of `docker-compose`
CAUTION! Configure A DNS record for your domain before deploying solution.

```yml
version: '3'

services:

  nginx-proxy:
    image: jwilder/nginx-proxy
    labels:
      - "com.github.jrcs.letsencrypt_nginx_proxy_companion.nginx_proxy=true"
    container_name: nginx-proxy
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./conf-nginx:/etc/nginx/conf.d
      - ./vhost:/etc/nginx/vhost.d
      - html:/usr/share/nginx/html
      - dhparam:/etc/nginx/dhparam
      - ./certs:/etc/nginx/certs:ro
      - /var/run/docker.sock:/tmp/docker.sock:ro

  letsencrypt:
    image: jrcs/letsencrypt-nginx-proxy-companion
    container_name: le_nginx
    volumes_from:
      - nginx-proxy
    volumes:
      - ./certs:/etc/nginx/certs:rw
      - /var/run/docker.sock:/var/run/docker.sock:ro
  open-balena-proxy:
    image: docker.pkg.github.com/markfieldman/open-balena-dashboard-backend/open-balena-dashboard-backend:latest
    environment:
      VIRTUAL_HOST: dashboard.example.com
      VIRTUAL_PORT: 6040
      LETSENCRYPT_HOST: dashboard.example.com
      LETSENCRYPT_EMAIL: my_awesome_mail@mail.com
      BALENA_URL: https://api.example.com
volumes:
  html:
  dhparam:
```
## Roadmap
1. Proxy for main requests ✅
2. Include Web UI inside project ✅
3. Add possibility to use it alongside with other servers ✅
4. Write `docker-compose` deployment example ✅
5. Add support of Web UI login for [balena CLI](https://github.com/balena-io/balena-cli) 🕒
6. Create dashboard with healthcheck charts of devices 🕒
7. Add role dividing on proxy level for sharing access to dashboard for another users using `balena api-key create` functionality but with restrictions at proxy level 🕒
