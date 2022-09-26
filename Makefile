build_datagen:
	cd tsplayer-datagen && docker build . -t markandreev/demo-tsplayer-datagen:latest

upload_datagen: build_datagen
	docker push markandreev/demo-tsplayer-datagen:latest

build_ui:
	cd tsplayer-ui && npm i && npm run build && cp bundle.min.js ../tsplayer-application/src/main/resources/META-INF/resources/

build_application:
	cd tsplayer-application && ./mvnw clean package -Dquarkus.container-image.build=true -Dquarkus.container-image.name=demo-tsplayer -Dquarkus.container-image.tag=latest -DskipTests=true

upload_application: build_application
	docker push markandreev/demo-tsplayer:latest
