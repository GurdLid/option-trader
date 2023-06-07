//
//  main.cpp
//  GBMStockSim
//
//  Created by Gurdeep Lidder on 28/05/2023.
//

#include <stdio.h>
#include <iostream>
#include <vector>
#include <random>
#include <fstream>

//Function prototype
std::vector<double> GBM(const double S0, const double r, const double sigma, const double T, const double N);

//Initialising random generator
static std::mt19937 rng(static_cast<unsigned int>(time(0)));

int main()
{
    //Input parameters
    const double S0 = 100.0;
    const double r = 0.05;
    const double sigma = 0.25;
    const double T = 20.0; //20 years
    const double N = 365.0*T; //steps of size = 1day

    std::vector<double> stockPriceValues = GBM(S0,r,sigma,T,N); //Vector to store prices
    
    std::ofstream writeFile; // Define output stream
    writeFile.open("/Users/gurdeeplidder/IdeaProjects/WileyEdge/Projects/optiontrading/GBMStockSim/stockprices.csv"); //Open file
    assert(writeFile.is_open()); // Check file is open
    for(double S : stockPriceValues)
    {
        writeFile << S << "\n";
    }
    writeFile.close();
    
    
    
    std::cout << stockPriceValues[stockPriceValues.size()-1] << std::endl;
    
    return 0;
}

std::vector<double> GBM(const double S0, const double r, const double sigma, const double T, const double N)
{
    std::vector<double> S;
    S.emplace_back(S0);
    double dt = T/N;
    
    std::normal_distribution<double> dist(0.0, 1.0);
    
    double driftTerm= (r-0.5*sigma*sigma)*dt;
    double dWTerm = sigma*sqrt(dt);
    double Z;
    
    for(int i = 1; i < N; i++)
    {
        Z = dist(rng);
        S.emplace_back(S[i-1] *exp(driftTerm + dWTerm*Z));
    }
    return S;
}
