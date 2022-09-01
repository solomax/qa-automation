cd target
mvn archetype:generate \
  --batch-mode \
  --settings ../development-maven-settings.xml \
  -Dmaven.wagon.http.ssl.insecure=true \
  -DarchetypeGroupId=dev.aherscu.qa \
  -DarchetypeArtifactId=qa-testing-archetype \
  -DgroupId=com.acme \
  -DartifactId=testing \
  -Dversion=1.0-SNAPSHOT \
  -Dpackage=com.acme.testing
