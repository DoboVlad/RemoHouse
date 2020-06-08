import { Pipe, PipeTransform } from '@angular/core';
import {Book} from "../shared/book.model";
@Pipe({
  name: 'filter'
})
export class FilterTitlePipe implements PipeTransform {
  transform(items: Book[], searchText: string): any[] {
    if(!items) return [];
    if(!searchText) return items;
    searchText = searchText.toLowerCase();
    return items.filter( it => {
      return it.title.toLowerCase().includes(searchText);
    });
  }
}
