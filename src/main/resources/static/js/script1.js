
const radioButtons = document.querySelectorAll('input[name="flexRadioDefault"]');
let fileSelector = document.getElementById("inputArchivo")
let imgpreview = document.getElementById("previewImg")


radioButtons.forEach(radio =>{
    document.addEventListener("change",function (){
        if (document.getElementById("rdPDF").checked){
            fileSelector.setAttribute("accept",".pdf");
        }
        if(document.getElementById("rdWord").checked){
            fileSelector.setAttribute("accept",".docx")

        }
        if(document.getElementById("rdImagen").checked){
            fileSelector.setAttribute("accept","image/*")
        }
    })
})

fileSelector.addEventListener("change",(event) =>{

    let elementToAppend = document.getElementById("hola")
    const files = event.target.files;
    elementToAppend.innerHTML = ""

    if (files){
        for(let i = 0; i < files.length; i++){
            const reader = new FileReader();
            let file = files[i]
            let img = document.createElement("img")
            img.id = "img" + i
            img.alt = file.name
            img.style.maxWidth = "150px"
            reader.onload = (e) =>{
                img.src = e.target.result
                img.style.display = "block"
                elementToAppend.appendChild(img);
            }
            reader.readAsDataURL(file);
        }
    }
    else{
        imgpreview.style.display = "none";
    }

} )

