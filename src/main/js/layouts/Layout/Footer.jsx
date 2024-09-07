import Logo from "../../components/Logo";

export function Footer() {
  return (
    <footer className="pb-16 pt-10">
      <div className="max-w-7xl mx-4 sm:mx-6 md:mx-8 border-t pt-10 border-t-slate-900 dark:border-t-slate-50/10">
        <Logo />
      </div>
    </footer>
  );
}