import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import {
  Chart,
  PolarAreaController,
  RadialLinearScale,
  ArcElement,
  Tooltip,
  Legend
} from 'chart.js';

Chart.register(
  PolarAreaController,
  RadialLinearScale,
  ArcElement,
  Tooltip,
  Legend
);
import * as echarts from 'echarts';
import { DashboardService } from '../services/DashboardService';
import { HistoriqueAchatService } from '../services/historique-achat';
import { ReclamationService } from '../services/reclamationService';
import { TechnicienService } from '../services/TechnicienServices';
import { InterventionService } from '../services/InterventionService';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, HeaderComponent ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit,AfterViewInit {
  totalTechniciens: number = 0;
  totalResponsables: number = 0;
  totalClients: number = 0;
  produitsReclames: any[] = [];
  stats: any;



  constructor(private dashboardService: DashboardService,
              private historiqueService: HistoriqueAchatService,
              private  reclamationService: ReclamationService ,
              private technicienService: TechnicienService,
              private interventionService: InterventionService
  ) {}

  ngOnInit(): void {
    this.dashboardService.getStats().subscribe(data => {
      this.totalTechniciens = data.totalTechniciens;
      this.totalResponsables = data.totalResponsables;
      this.totalClients = data.totalClients;
  
      this.initBarChart(); // OK ici car totalClients est prêt
  
      this.reclamationService.getProduitsLesPlusReclames().subscribe(data => {
        this.produitsReclames = data;
      });
  
      this.interventionService.getStats().subscribe((data: any) => {
        this.stats = data;
        this.initBudgetChart(data); // Appel ici et non dans ngAfterViewInit
      });
    });

    history.pushState(null, '', location.href);
    window.onpopstate = function () {
      history.go(1); // Annule le retour arrière
    };
  }
  
  ngAfterViewInit(): void {
    this.initLineChart();
    this.initAreaChart();
    this.initReportsChart();
    this.createPolarAreaChart();
  }

  initLineChart() {
    const orderedDays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  
    this.historiqueService.getAchatsParJourSemaine().subscribe((data: { [x: string]: any; }) => {
      const values = orderedDays.map(day => data[day] || 0); // Remplir avec 0 si absent
  
      const chartDom = document.getElementById('lineChart')!;
      const chart = echarts.init(chartDom);
      
      chart.setOption({
        title: { text: `Achats par ${this.periode}` },
        tooltip: {},
        xAxis: { type: 'category', data: orderedDays },
        yAxis: {
          type: 'value',
          minInterval: 1,  // Évite les décimales sur l'axe Y
          axisLabel: {
            formatter: (value: number) => Math.floor(value) // Arrondir à l'entier
          }
        },
        series: [{
          name: 'Achats',
          type: 'line',
          data: values
        }]
      });
    });
  }
  
  
  
  
  
  periode: string = 'mois'; // 'jour', 'semaine', 'mois', 'annee'

  initAreaChart() {
    this.reclamationService.getReclamationsByPeriode(this.periode).subscribe(data => {
      const labels = Object.keys(data);
      const values = Object.values(data);
    
      const chartDom = document.getElementById('areaChart')!;
      const chart = echarts.init(chartDom);
    
      chart.setOption({
        title: { text: `Réclamations par ${this.periode}` },
        tooltip: {},
        xAxis: { type: 'category', data: labels },
        yAxis: {
          type: 'value',
          minInterval: 1  // ✅ Évite les décimales
        },
        series: [{
          name: 'Réclamations',
          type: 'line',
          areaStyle: {},
          data: values
        }]
      });
    });
  }    
  
  

  initBarChart() {
    const chartDom = document.getElementById('barChart')!;
    const chart = echarts.init(chartDom);
    chart.setOption({
      title: { text: 'Ressources humaines' },
      tooltip: {},
      xAxis: { 
        type: 'category', 
        data: ['Clients', 'Techniciens', 'Responsables'] 
      },
      yAxis: { 
        type: 'value',
        minInterval: 1 // ✅ Empêche les valeurs décimales
      },
      series: [{
        data: [this.totalClients, this.totalTechniciens, this.totalResponsables],
        type: 'bar'
      }]
    });
  }
  

  

  initReportsChart() {
    const chartDom = document.getElementById('reportsChart')!;
    const chart = echarts.init(chartDom);
  
    const days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  
    this.reclamationService.getReclamationsStatsParJour().subscribe((data: any) => {
      const enAttente = days.map(day => data[day]?.EN_ATTENTE || 0);
      const enCours = days.map(day => data[day]?.EN_COURS || 0);
      const terminee = days.map(day => data[day]?.RESOLUE || 0);
  
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['En attente', 'En cours', 'Términée'] },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: days
        },
        yAxis: {
          type: 'value',
          minInterval: 1
        },
        series: [
          {
            name: 'En attente',
            type: 'line',
            data: enAttente,
            areaStyle: {}
          },
          {
            name: 'En cours',
            type: 'line',
            data: enCours,
            areaStyle: {}
          },
          {
            name: 'Términée',
            type: 'line',
            data: terminee,
            areaStyle: {}
          }
        ]
      });
    });
  }
  


  initBudgetChart(stats: any) {
    const chartDom = document.getElementById('budgetChart')!;
    const chart = echarts.init(chartDom);
    
    chart.setOption({
      title: {
        text: 'Indicateurs Qualité de Service SAV'
      },
      tooltip: {},
      legend: {
        data: ['Performance Actuelle']
      },
      radar: {
        indicator: [
          { name: 'Interventions terminées', max: 100 },
          { name: 'Durée moyenne (h)', max: 10 },
          { name: 'Pièces / intervention', max: 10 },
          { name: 'Réclamations traitées', max: 100 },
          { name: 'Temps d\'attente (j)', max: 5 },
          { name: 'Taux de retard (%)', max: 100 }
        ]
      },
      series: [{
        name: 'Qualité SAV',
        type: 'radar',
        data: [
          {
            value: [
              stats.nbTerminees,
              stats.dureeMoyenne,
              stats.piecesParIntervention,
              stats.reclamationsTraitees,
              stats.tempsAttenteMoyen,
              stats.tauxRetard
            ],
            name: 'Performance Actuelle'
          }
        ]
      }]
    });
  }
  

  createPolarAreaChart(): void {
    this.technicienService.getTechniciensCountBySpecialite().subscribe(data => {
      const labels = Object.keys(data);
      const values = Object.values(data);

      const backgroundColors = [
        'rgb(255, 99, 132)',
        'rgb(75, 192, 192)',
        'rgb(255, 205, 86)',
        'rgb(201, 203, 207)',
        'rgb(54, 162, 235)',
        'rgb(153, 102, 255)',  // Add more if needed
      ];

      const ctx = document.getElementById('polarAreaChart') as HTMLCanvasElement;
      new Chart(ctx, {
        type: 'polarArea',
        data: {
          labels: labels,
          datasets: [{
            label: 'Nombre de techniciens par spécialité',
            data: values,
            backgroundColor: backgroundColors.slice(0, labels.length)
          }]
        }
      });
    });
  }
}
