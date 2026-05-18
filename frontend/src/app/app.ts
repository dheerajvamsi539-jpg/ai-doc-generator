import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { marked } from 'marked';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class AppComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private sanitizer = inject(DomSanitizer);

  generatedDocument: string | null = null;
  renderedHtml: SafeHtml | null = null;
  isLoading = false;
  isPdfLoading = false;

  appForm = this.fb.group({
    projectName: ['', Validators.required],
    description: ['', Validators.required],
    targetAudience: [''],
    keyFeatures: ['', Validators.required],
    techStack: ['']
  });

  onSubmit() {
    if (this.appForm.valid) {
      this.isLoading = true;
      console.log('Sending request to generate document...');
      this.http.post<{document: string}>('http://localhost:8080/api/documents/generate', this.appForm.value)
        .subscribe({
          next: async (response) => {
            console.log('Document received successfully');
            this.generatedDocument = response.document;
            const html = await marked.parse(this.generatedDocument);
            this.renderedHtml = this.sanitizer.bypassSecurityTrustHtml(html);
            this.isLoading = false;
          },
          error: (err) => {
            console.error('Error generating document', err);
            this.isLoading = false;
          }
        });
    }
  }

  copyToClipboard() {
    if (this.generatedDocument) {
      navigator.clipboard.writeText(this.generatedDocument).then(() => {
        alert('Prompt copied to clipboard!');
      }).catch(err => {
        console.error('Could not copy text: ', err);
      });
    }
  }

  downloadPdf() {
    if (this.appForm.invalid || !this.generatedDocument) return;

    this.isPdfLoading = true;
    this.http.post('http://localhost:8080/api/documents/pdf', this.appForm.value, {
      responseType: 'blob',
      observe: 'response'
    }).subscribe({
      next: (response) => {
        const blob = response.body;
        if (blob) {
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          const contentDisposition = response.headers.get('Content-Disposition');
          let fileName = 'AI_App_Spec.pdf';
          if (contentDisposition) {
            const fileNameMatch = contentDisposition.match(/filename="?([^"]+)"?/);
            if (fileNameMatch && fileNameMatch.length === 2) {
              fileName = fileNameMatch[1];
            }
          }
          link.download = fileName;
          link.click();
          window.URL.revokeObjectURL(url);
        }
        this.isPdfLoading = false;
      },
      error: (err) => {
        console.error('Error downloading PDF', err);
        alert('Failed to download PDF from server.');
        this.isPdfLoading = false;
      }
    });
  }
}
