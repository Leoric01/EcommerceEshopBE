mainPackage="com.leoric.ecommerceshopbe"
rootPath="$(dirname "$(realpath "$0")")"
entityPackage="${mainPackage}.models"
serviceInterfacePackage="${mainPackage}.services.interfaces"
entityFolderPath="$rootPath/src/main/java/$(echo $mainPackage | tr '.' '/')/models"
serviceInterfaceFolderPath="$rootPath/src/main/java/$(echo $mainPackage | tr '.' '/')/services/interfaces"

mkdir -p "$serviceInterfaceFolderPath"

for entityFile in "$entityFolderPath"/*.java; do
    entity=$(basename "$entityFile" .java)

    serviceInterfaceFileName="$serviceInterfaceFolderPath/${entity}Service.java"
    echo "Generating $serviceInterfaceFileName..."

    cat > "$serviceInterfaceFileName" <<EOL
package $serviceInterfacePackage;

import java.util.List;
import $entityPackage.$entity;
import org.springframework.stereotype.Service;

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
