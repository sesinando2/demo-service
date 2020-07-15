 # Spring Boot Template

This is a template application for Spring Boot REST application instrumented to
expose Prometheus metrics. This is project is meant to be the base template for all micro service applications.

Before starting please read https://www.12factor.net.

## Requirements:
- Java 11
- Maven
---
## Running template demo
```
mvn spring-boot:run -Dspring-boot.run.profiles=template
```

## Running template tests & build
```
mvn clean test -Dtemplate
mvn clean install -Dtemplate
```
---
## How to use:
Create a new repository for the new service. (e.g. `authentication-service`).

Clone this project and name it after the new service.
```
git clone https://github.com/Ibenta/springboot-template.git authentication-service
```

Update the url of the origin to the newly created repository.
```
git remote set-url origin https://github.com/Ibenta/authentication-service.git
```

Fetch origin.
```
git fetch origin
```

Push to repository.
```
git push -f --set-upstream origin master
```
---
## Downstream merge
From time to time, there will be changes to this project that should will affect common functionality among services. These changes will have to be merged down stream. To do this:

Create an remote upstream.
```
git remote add upstream https://github.com/Ibenta/springboot-template.git
```

Fetch upstream.
```
git fetch upstream
```

Merge upstream.
```
git merge upstrea/master
```
---
## GitOps
```
jx create issue -t 'Do something cool'
```
```
git checkout -b feature/1-Do-something-cool
```
```
git commit -m '#1 Do something cool'
```
```
git push --set-upstream origin feature/1-Do-something-cool
```
```
jx create pr -t 'Do something cool'
```
Watch the magic happens :)

---
## Jenkins X
This template is made to run on Jenkins X.

To import this project to Jenkins X:
```
jx import
```

### DevPods
A dev pod is you're own kubernetes pod that you can use during development.

VS Code Server
```
jx create devpod
```
Desktop IDE
```
jx sync
jx create devpod --reuse --sync
```
Bash Shell
```
jx rsh -d
```
### Promote Environment
```
jx promote --version v0.0.1 --env production --timeout 1h
```
---
## Endpoints used by Kubernetes

This quickstart exposes the following endpoints important for Kubernetes deployments:
- `/actuator/health` - Spring Boot endpoint indicating health. Used by Kubernetes as readiness probe.
- `/actuator/metrics` - Prometheus metrics. Invoked periodically and collected by Prometheus Kubernetes scraper.
