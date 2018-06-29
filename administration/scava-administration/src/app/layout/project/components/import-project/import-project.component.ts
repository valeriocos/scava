import { Component, OnInit } from '@angular/core';
import { Project } from '../../project.model';
import { ImportProjectService } from '../../../../shared/services/api-server/import-project.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-import-project',
    templateUrl: './import-project.component.html',
    styleUrls: ['./import-project.component.scss']
})
export class ImportProjectComponent implements OnInit {

    private project: Project = {};
    private isSaving: boolean;

    constructor(
        private router: Router,
        private importProjectService: ImportProjectService
    ) { }

    ngOnInit() {
        this.isSaving = false;
    }

    save() {
        this.isSaving = true;
        this.importProjectService.importProject(this.project).subscribe(resp => {
            console.log(resp);
        }, error => {
            console.log(error);
        });
    }

}