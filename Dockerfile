FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace/app

COPY . /workspace/app
RUN --mount=type=cache,target=/root/.gradle ./gradlew clean build

FROM eclipse-temurin:17-jdk AS runner
VOLUME /tmp

COPY --from=build /workspace/app/build/libs/* /app/lib/

CMD ["java", "-jar", "/app/lib/spring_boilerplate.jar", "--spring.profiles.active=${ACTIVE_PROFILE}"]