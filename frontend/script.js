document.getElementById('uploadForm').onsubmit = async function (e) {
  e.preventDefault();
  const formData = new FormData(this);
  const res = await fetch('http://localhost:8080/upload', {
    method: 'POST',
    body: formData,
  });
  alert(await res.text());
};