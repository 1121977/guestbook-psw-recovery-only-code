let password, password2
document.getElementById("password").addEventListener("change", (e)=>{password = e.target.value})
document.getElementById("password2").addEventListener("change", (e)=>{password2 = e.target.value})
function passwordCheck(){
    if(password.length != 0 && password2.length !=0 && password != password2){
        alert("Пароли не совпадают!")
        return false
    }
}
document.querySelector("form").onsubmit = passwordCheck
