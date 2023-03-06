import { NgbPaginationNumber } from "@ng-bootstrap/ng-bootstrap"
import {Byte} from "@angular/compiler/src/util";

export interface Screenshot {
    image: string,
    screenshotId: number,
    examineeId: number,
    examId: number,
    file: Byte[]
}
