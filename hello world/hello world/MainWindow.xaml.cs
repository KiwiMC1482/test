using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace hello_world
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void btnhello_Click(object sender, RoutedEventArgs e)
        {
            lblhello.Content = "Hello World!";
        }

        private void btnok_Click(object sender, RoutedEventArgs e)
        {
            lblhello.Content = "Hello Kira!";
            //messgae box code
           var results = MessageBox.Show("Hello World", "hello",MessageBoxButton.YesNo,MessageBoxImage.Question);
            if (results == MessageBoxResult.Yes)
            {
                lblhello.Content = "Hello Ross and Kai";
            }
        }

        private void btnSteven_Click(object sender, RoutedEventArgs e)
        {
            lblhello.Content = "Hello Steven";
            
        }
    }
}
