rootPath="$(dirname "$(realpath "$0")")"
srcPath="$rootPath/src/main/java"
mainPackage=$(find "$srcPath" -type d -mindepth 1 -maxdepth 1 | xargs basename | tr '/' '.')
entityPackage="${mainPackage}.models"
serviceInterfacePackage="${mainPackage}.services.interfaces"
entityFolderPath="$srcPath/$(echo $mainPackage | tr '.' '/')/models"
serviceInterfaceFolderPath="$srcPath/$(echo $mainPackage | tr '.' '/')/services/interfaces"
mkdir -p "$serviceInterfaceFolderPath"
for entityFile in "$entityFolderPath"/*.java; do
    entity=$(basename "$entityFile" .java)

    serviceInterfaceFileName="$serviceInterfaceFolderPath/${entity}Service.java"
    echo "Generating $serviceInterfaceFileName..."

    cat > "$serviceInterfaceFileName" <<EOL
package $serviceInterfacePackage;

import java.util.List;
import $entityPackage.$entity;
import $serviceInterfacePackage.${entity}Service;

@Service
public interface ${entity}Service {

    List<$entity> findAll();
    $entity findById(Long id);
    $entity save($entity entity);
    void deleteById(Long id);
}
EOL

done

echo "Service Interface files generated successfully!"
