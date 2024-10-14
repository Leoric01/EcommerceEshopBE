# Set your entity and repository folder paths
$entityPackage = "com.leoric.ecommerceshopbe.models"
$repositoryPackage = "com.leoric.ecommerceshopbe.repositories"
$entityFolderPath = "C:\Users\trogl\Documents\Git\EcommerceShopBe\E-CommerceShopBE\src\main\java\com\leoric\ecommerceshopbe\models"
$repositoryFolderPath = "C:\Users\trogl\Documents\Git\EcommerceShopBe\E-CommerceShopBE\src\main\java\com\leoric\ecommerceshopbe\repositories"

# List of entities for which you want to generate repositories
$entities = Get-ChildItem -Path $entityFolderPath -Filter "*.java" | ForEach-Object {
    $_.BaseName
}
# Loop through each entity and generate repository files
foreach ($entity in $entities)
{
    $fileName = "${repositoryFolderPath}\${entity}Repository.java"
    Write-Host "Generating $fileName..."

    # Write content to the new repository file
    $fileContent = @"
package $repositoryPackage;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import $entityPackage.$entity;

@Repository
public interface ${entity}Repository extends JpaRepository<$entity, Long> {
}
"@

    # Create the file and write the content
    $fileContent | Out-File -FilePath $fileName -Encoding utf8
}

Write-Host "Repository files generated successfully!"