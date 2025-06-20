window.addEventListener('DOMContentLoaded', () => {
  console.log('Script loaded, binding click event');

  // Create a dedicated container for the link to isolate it
  const linkContainer = document.createElement('div');
  linkContainer.id = 'persistentLinkContainer';
  document.querySelector('.container').appendChild(linkContainer);

  const fileInput = document.getElementById('fileInput');
  const status = document.getElementById('status');
  const uploadBtn = document.getElementById('uploadBtn');
  let lastDownloadUrl = '';

  uploadBtn.addEventListener('click', async (event) => {
    event.preventDefault();
    event.stopPropagation();
    console.log('Upload button clicked');

    const file = fileInput.files[0];
    if (!file) {
      status.textContent = 'Please select a file to upload.';
      status.classList.add('error');
      console.log('No file selected');
      return;
    }

    status.textContent = 'Uploading...';
    status.classList.remove('error');
    linkContainer.textContent = '';
    console.log('Starting upload request');

    const formData = new FormData();
    formData.append('file', file);

    try {
      const res = await fetch('http://localhost:8080/upload', {
        method: 'POST',
        headers: {
          Authorization: 'Bearer secret123',
        },
        body: formData,
      });

      console.log('Response status:', res.status, res.statusText);
      const text = await res.text();
      console.log('Response body:', text);
      if (!res.ok) throw new Error(text || 'Bad request, no details');

      const token = text.match(/[a-f0-9\-]{36}/)?.[0];
      if (!token) throw new Error('Token not found in response');

      lastDownloadUrl = `http://localhost:8080/download/${token}`;
      linkContainer.innerHTML = `<a href="${lastDownloadUrl}" target="_blank">${lastDownloadUrl}</a>`;
      status.textContent = 'Upload successful!';
      fileInput.value = '';
      console.log('Link displayed in persistentLinkContainer:', lastDownloadUrl);

      // Attempt to prevent reload after success
      window.onbeforeunload = (e) => {
        console.log('Preventing reload, event:', e);
        e.preventDefault();
        e.returnValue = ''; // Required for some browsers
        return ''; // Legacy support
      };
    } catch (error) {
      console.error('Upload error:', error.message);
      status.textContent = `Upload failed: ${error.message}`;
      status.classList.add('error');
    }
  });

  // Debug: Catch page reloads
  window.addEventListener('beforeunload', (e) => {
    console.log('Page is reloading, event:', e);
  });
});