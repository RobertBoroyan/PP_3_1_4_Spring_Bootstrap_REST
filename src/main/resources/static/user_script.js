async function auth() {
    let res = await fetch('http://localhost:8080/user/api');
    return await res.json();
}

upperPanel();
refreshUserPanel();

//Заполнение верхней панели
async function upperPanel() {
    let user = await auth();
    document.getElementById("adminUsername").textContent = user.username;
    let roles = "";
    user.roles.forEach(role => {
        roles += role.role.substring(5, role.role.length) + " ";
    })
    document.getElementById("adminRoles").textContent = roles;
}

//Обновление панели юзера
async function refreshUserPanel() {
    const tbody = document.querySelector('#userTBody');

    let user = await auth();
    let roles = user.roles.map(role => role.role.substring(5, role.role.length));
    let rolesInTable = '';
    roles.forEach(role => {
        rolesInTable += `<div>${role}</div>`
    });

    tbody.innerHTML = `<tr>
            <td class="align-middle">${user.id}</td>
            <td class="align-middle">${user.name}</td>
           
            <td class="align-middle">${user.age}</td>
        
            <td class="align-middle">${user.username}</td>
            <td class="align-middle">${rolesInTable}</td>
            </tr>`;
}
