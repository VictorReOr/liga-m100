import { ButtonHTMLAttributes, forwardRef } from 'react';
import { cn } from '../../lib/utils';

export interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: 'primary' | 'secondary' | 'danger';
}

const Button = forwardRef<HTMLButtonElement, ButtonProps>(
    ({ className, variant = 'primary', ...props }, ref) => {
        const variants = {
            primary: 'bg-blue-600 text-white hover:bg-blue-700',
            secondary: 'bg-gray-200 text-gray-900 hover:bg-gray-300',
            danger: 'bg-red-600 text-white hover:bg-red-700',
        };

        return (
            <button
                ref={ref}
                className={cn(
                    'px-4 py-2 rounded-md font-medium transition-colors disabled:opacity-50',
                    variants[variant],
                    className
                )}
                {...props}
            />
        );
    }
);

Button.displayName = 'Button';
export default Button;
