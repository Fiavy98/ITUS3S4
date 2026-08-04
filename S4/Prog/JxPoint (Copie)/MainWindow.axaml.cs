using Avalonia.Controls;

namespace JxPoint;

public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
        this.KeyDown += MainWindow_KeyDown;
    }

    private void MainWindow_KeyDown(object? sender, Avalonia.Input.KeyEventArgs e)
    {
        if (this.Content is JxPoint.Views.Plateaux plateaux)
        {
            plateaux.HandleKeyShortcut(e);
            if (e.Handled)
                return;
        }
    }
}