export interface IPomoc {
  id?: number;
}

export class Pomoc implements IPomoc {
  constructor(public id?: number) {}
}

export function getPomocIdentifier(pomoc: IPomoc): number | undefined {
  return pomoc.id;
}
