document.querySelector("#uploadForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const baseurl = "http://localhost:8080/upload";
  const formData = new FormData(e.target);

  try { 
    const response = await fetch(baseurl, {
      method: "POST",
      body: formData
    });  

    if(!response.ok) {
      alert("Error uploading file. Please try again.");
      return;
    }else{
      alert("File uploaded successfully!");
    }
  } catch (error) {
    alert("An error occurred while uploading the file: " + error.message);
  }

})