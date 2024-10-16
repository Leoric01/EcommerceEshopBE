mainPackage="com.leoric.ecommerceshopbe"
rootPath="$(dirname "$(realpath "$0")")"
entityPackage="${mainPackage}.models"
repositoryPackage="${mainPackage}.repositories"
serviceInterfacePackage="${mainPackage}.services.interfaces"
serviceImplPackage="${mainPackage}.services.impl"
entityFolderPath="$rootPath/src/main/java/$(echo $mainPackage | tr '.' '/')/models"
serviceImplFolderPath="$rootPath/src/main/java/$(echo $mainPackage | tr '.' '/')/services/impl"

mkdir -p "$serviceImplFolderPath"

for entityFile in "$entityFolderPath"/*.java; do
    entity=$(basename "$entityFile" .java)

    serviceImplFileName="$serviceImplFolderPath/${entity}ServiceImpl.java"
    echo "Generating $serviceImplFileName..."

    cat > "$serviceImplFileName" <<EOL
package $serviceImplPackage;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import $repositoryPackage.${entity}Repository;
import $entityPackage.$entity;
import $serviceInterfacePackage.${entity}Service;

@Service
@RequiredArgsConstructor
public class ${entity}ServiceImpl implements ${entity}Service {

    private final ${entity}Repository ${entity,,}Repository;

    @Override
    public List<$entity> findAll() {
        return ${entity,,}Repository.findAll();
    }

    @Override
    public $entity findById(Long id) {
        return ${entity,,}Repository.findById(id)
            .orElseThrow(() -> new RuntimeException("$entity not found"));
    }

    @Override
    public $entity save($entity entity) {
        return ${entity,,}Repository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        ${entity,,}Repository.deleteById(id);
    }
}
EOL

done

echo "ServiceImpl files generated successfully!"
