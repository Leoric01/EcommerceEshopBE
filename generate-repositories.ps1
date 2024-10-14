# Set your main package (root package for the project)
$mainPackage = "com.leoric.ecommerceshopbe"

# Get the root directory where the script is located (project root)
$rootPath = $PSScriptRoot

# Set relative paths for the entity and repository directories
$entityPackage = "$mainPackage.models"
$repositoryPackage = "$mainPackage.repositories"
$entityFolderPath = Join-Path $rootPath "src\main\java\$( $mainPackage -replace '\.', '\\' )\models"
$repositoryFolderPath = Join-Path $rootPath "src\main\java\$( $mainPackage -replace '\.', '\\' )\repositories"

# Get all Java files in the models directory (extract the base filenames)
$entities = Get-ChildItem -Path $entityFolderPath -Filter "*.java" | ForEach-Object {
    $_.BaseName
}

# Loop through each entity (Java class) and generate repository files
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
