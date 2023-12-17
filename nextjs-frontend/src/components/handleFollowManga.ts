interface parameters{
  id: any,
  title: string,
  image: any
}
export function AddToFollowList(data: any): number {
  if (!localStorage.getItem("manga")) {
    localStorage.setItem("manga", JSON.stringify([{data}]));
    return 1;
  }
  let datalocal: any = localStorage.getItem("manga");
  const ArrayData: any = JSON.parse(datalocal);
  let key: boolean = false;
  ArrayData.forEach((element: any) => {
    if (element?.data.title === data?.title) {
      key = true;
      return;
    }
  });
  if (key) {
    return 0;
  } else {
    ArrayData.push({
      data
    });
  }
  localStorage.setItem("manga", JSON.stringify(ArrayData));
  return 1;
}
export function RemoveFromFollow(data: any): number {
  if (!localStorage.getItem("manga")) {
    return 0;
  }
  let datalocal: any = localStorage.getItem("manga");
  let ArrayData: any = JSON.parse(datalocal);
  let key: boolean = false;

  ArrayData.forEach((element: any, index: number) => {
    console.log("element ", element);
    if (element?.data.title === data.title) {
      ArrayData.splice(data?.title, 1);
      key = true;
      return;
    }
  });
  if (!key) {
    return 0;
  }
  localStorage.setItem("manga", JSON.stringify(ArrayData));
  return 1;
}

export function IsExistFollow(data: any): boolean {
  if (!localStorage.getItem("manga")) {
    return false;
  }
  let datalocal: any = localStorage.getItem("manga");
  const ArrayData: any = JSON.parse(datalocal);
  let key: boolean = false;

  ArrayData.forEach((element: any) => {
    if (element?.data.title === data?.title) {
      key = true;
      return;
    }
  });
  if (!key) {
    return false;
  }
  return true;
}
