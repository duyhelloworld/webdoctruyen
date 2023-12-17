"use client";
export default function Home(prop: any) {
  const param = prop.params.id;
  return (
    <>
      <div className="editmanga_container">{param}</div>
    </>
  );
}
