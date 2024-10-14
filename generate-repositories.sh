mainPackage="com.leoric.ecommerceshopbe"
rootPath="$(dirname "$(realpath "$0")")"
entityPackage="${mainPackage}.models"
repositoryPackage="${mainPackage}.repositories"
entityFolderPath="$rootPath/src/main/java/$(echo $mainPackage | tr '.' '/')/models"
repositoryFolderPath="$rootPath/src/main/java/$(echo $mainPackage | tr '.' '/')/repositories"

mkdir -p "$repositoryFolderPath"
for entityFile in "$entityFolderPath"/*.java; do
    entity=$(basename "$entityFile" .java)
    fileName="$repositoryFolderPath/${entity}Repository.java"
    echo "Generating $fileName..."

    cat > "$fileName" <<EOL
package $repositoryPackage;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import $entityPackage.$entity;

@Repository
public interface ${entity}Repository extends JpaRepository<$entity, Long> {
}
EOL
done

echo "Repository files generated successfully!"