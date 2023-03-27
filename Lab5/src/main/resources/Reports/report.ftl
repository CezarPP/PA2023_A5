<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document Catalog Report</title>
</head>
<body>
<h1>Document Catalog Report</h1>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Tags</th>
    </tr>
    </thead>
    <tbody>
    <#list documents as document>
        <tr>
            <td>${document.id}</td>
            <td>${document.name}</td>
            <td>
                <ul>
                    <#list document.tags?keys as tagKey>
                        <li>${tagKey}: ${document.tags[tagKey]}</li>
                    </#list>
                </ul>
            </td>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>
