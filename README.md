# OpenBalena backend for Dashboard
#### TL;DR
Dockerized back-end proxy for [OpenBalena](https://www.balena.io/open/) project.
Front-end from [Razikus/open-balena-dashboard](https://github.com/Razikus/open-balena-dashboard)
## Usage 
Main purpose of this project is proxying requests to open-balena-api without the TLS issues from self-signed CA.
You can use it with, or without front-end part, which are included in this solution.
## Example of `docker-compose`
TBD
## Roadmap
1. Proxy for main requests ✅
2. Include Web UI inside project ✅
3. Add possibility to use it alongside with other servers ✅
4. Write `docker-compose` deployment example 🕒
5. Add support of Web UI login for [balena CLI](https://github.com/balena-io/balena-cli) 🕒
6. Create dashboard with healthcheck charts of devices 🕒
7. Add role dividing on proxy level for sharing access to dashboard for another users using `balena api-key create` functionality but with restrictions at proxy level 🕒
