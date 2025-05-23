package fcai.prospera.service;

import fcai.prospera.model.*;
import fcai.prospera.repository.AssetRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

 /**
 * Handles the logic for generating reports
 */
public class ReportGenerationService {
    private final AssetRepository assetRepo;

    /**
     * Constructor for ReportGenerationService
     *
     * @param assetRepo the asset repository
     */
    public ReportGenerationService(AssetRepository assetRepo) {
        this.assetRepo = assetRepo;
    }

    /**
     * Generates a report for a given user based on the specified type and format.
     *
     * @param user   the user for whom the report is generated
     * @param type   the type of the report (e.g., PORTFOLIO)
     * @param format the format of the report (e.g., "PDF", "Excel")
     * @return a Report object containing the generated report data
     */
    public Report generateReport(User user, ReportType type, String format) {
        List<Asset> assets = assetRepo.getUserAssets(user.getId());
        BigDecimal netWorth = assetRepo.calculateNetWorth(user.getId());
        Map<AssetType, BigDecimal> distribution = assetRepo.getUserAssetDistribution(user.getId());

        ReportData reportData = new ReportData();
        reportData.setUser(user);
        reportData.setType(type);
        reportData.setAssets(assets);
        reportData.setNetWorth(netWorth);
        reportData.setAssetDistribution(distribution);

        Report report;
        if (format.equalsIgnoreCase("PDF")) {
            report = new PDFReport();
        }
        else {
            report = new ExcelReport();
        }
        report.generateData(reportData);
        return report;
    }
}